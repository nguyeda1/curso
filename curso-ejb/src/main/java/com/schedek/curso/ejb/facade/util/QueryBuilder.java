package com.schedek.curso.ejb.facade.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.jpa.JpaQuery;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.eclipse.persistence.sessions.DatabaseSession;

public class QueryBuilder<T> {

    protected final EntityManager em;
    protected final Class entityClass;
    protected final javax.persistence.criteria.CriteriaBuilder cb;
    protected final javax.persistence.criteria.CriteriaQuery cq;
    private final Map<Class, Root<T>> roots = new HashMap<>();

    public QueryBuilder(EntityManager em, Class entityClass) {
        this.em = em;
        this.entityClass = entityClass;
        cb = em.getCriteriaBuilder();
        cq = cb.createQuery();
    }

    protected Class getIDClass() {
        return em.getMetamodel().entity(entityClass).getIdType().getJavaType();
    }

    public int count(Map<String, Object> filters) {
        cq.where(cb.and(getPredicate(), getFilter(filters)));
        cq.select(em.getCriteriaBuilder().countDistinct(getRoot(entityClass)));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();

    }

    public List<T> loadAll() {
        return (List<T>) em.createQuery(cb.createQuery().select(getRoot(entityClass)).orderBy(getDefaultOrder()).where(getPredicate()).distinct(true)).getResultList();
    }

    public List<T> load(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters) {
        cq.orderBy(getOrder(sortField, asc));
        cq.where(cb.and(getPredicate(), getFilter(filters)));
        cq.select(getRoot(entityClass)).distinct(true);
        TypedQuery tq = em.createQuery(cq);

        DatabaseQuery dq = tq.unwrap(JpaQuery.class).getDatabaseQuery();
        DatabaseSession sess = JpaHelper.getDatabaseSession(em.getEntityManagerFactory());
        dq.prepareCall(sess, new DatabaseRecord());
        String sql = dq.getSQLString();
        //dq.getSQLStatement().buildCall(dq.getExecutionSession()).getSQLString()
        tq = em.createQuery(cq);
        if (pageSize > 0) {
            tq.setFirstResult(first);
            tq.setMaxResults(pageSize);
        }
        return tq.getResultList();
    }

    protected javax.persistence.criteria.Predicate getPredicate() {
        return cb.conjunction();
    }

    protected javax.persistence.criteria.Predicate getFilter(Map<String, Object> filters) {
        Root<T> r = getRoot(entityClass);
        javax.persistence.criteria.Predicate p = cb.conjunction();
        if (filters != null) {
            for (Map.Entry<String, Object> entry : new HashMap<String, Object>(filters).entrySet()) {
                String kn = entry.getKey();
                Object value = entry.getValue();
                if (value == null) {
                    filters.remove(kn);
                    continue;
                }
                AdvancedFilter af = (value instanceof AdvancedFilter)
                        ? (AdvancedFilter) value
                        : new AdvancedFilter(MatchMode.STARTS_WITH_MATCH_MODE, value);
                if (af.getValue() instanceof String && ((String) af.getValue()).isEmpty()) {
                    continue;
                }
                PathInfo cpe = getPath(r, entityClass, kn);
                Path qp = cpe.p;
                String k = cpe.n;

                Class c = em.getMetamodel().entity(cpe.c).getDeclaredAttribute(k).getJavaType();

                if (c == null) {
                    Logger.getLogger(QueryBuilder.class.getName()).info("Unknown attribute " + k + " for entity " + entityClass.getSimpleName());
                    continue;
                }
                Predicate q = cb.disjunction();
                Object[] qps = af.getQueryParts();
                if (qps.length > 0) {
                    for (Object qv : qps) {
                        try {
                            q = cb.or(q, buildFilterPredicate(c, af, qp, qv, qps.length == 1));
                        } catch (UnsupportedFilterColumnTypeException ex) {
                            Logger.getLogger(QueryBuilder.class.getName()).info("Unknown attribute " + k + " for entity " + entityClass.getSimpleName());
                        }
                    }
                    p = cb.and(q, p);
                }
            }
        }
        return p;
    }

    protected Predicate buildFilterPredicate(Class c, AdvancedFilter af, Path qp, Object qv, boolean single) throws UnsupportedFilterColumnTypeException {
        Predicate qq = cb.disjunction();
        if (String.class.equals(c)) {
            switch (af.getMatchMode()) {
                case IN_MODE:
                case EQUALS_MODE:
                case EXACT_MATCH_MODE:
                    qq = cb.equal(qp, qv);
                    break;
                case GLOBAL_MODE:
                case CONTAINS_MATCH_MODE:
                    qq = cb.like(cb.lower(qp.as(String.class)), "%" + qv.toString().toLowerCase() + "%");
                    break;
                case ENDS_WITH_MATCH_MODE:
                    qq = cb.like(cb.lower(qp.as(String.class)), "%" + qv.toString().toLowerCase());
                    break;
                case STARTS_WITH_MATCH_MODE:
                default:
                    qq = cb.like(cb.lower(qp.as(String.class)), qv.toString().toLowerCase() + "%");
            }
        } else if (Long.class.equals(c) || Integer.class.equals(c) || BigDecimal.class.equals(c)) {
            switch (af.getMatchMode()) {
                case IN_MODE:
                    qv = qv.toString().trim();
                    //interval mode
                    if (single && (qv.toString().startsWith("(") || qv.toString().startsWith("<"))
                            && (qv.toString().endsWith(")") || qv.toString().endsWith(">"))
                            && qv.toString().contains(";")) {
                        try {
                            long a = Long.parseLong(qv.toString().substring(1, qv.toString().indexOf(";")));
                            long b = Long.parseLong(qv.toString().substring(qv.toString().indexOf(";") + 1, qv.toString().length() - 1));
                            Expression ex = qp.as(Number.class);
                            qq = cb.and(
                                    qv.toString().startsWith("(") ? cb.gt(ex, a) : cb.ge(ex, a),
                                    qv.toString().endsWith(")") ? cb.lt(ex, b) : cb.le(ex, b)
                            );
                        } catch (NumberFormatException ex) {
                        }
                        break;
                    }
                    try {
                        Long.parseLong(qv.toString());
                    } catch (NumberFormatException ex) {
                        break;
                    }
                case EQUALS_MODE:
                case EXACT_MATCH_MODE:
                    qq = cb.equal(qp, qv);
                    break;
                case GLOBAL_MODE:
                case CONTAINS_MATCH_MODE:
                    qq = cb.like(cb.function("tostring", String.class, qp), "%" + qv + "%");
                    break;
                case ENDS_WITH_MATCH_MODE:
                    qq = cb.like(cb.function("tostring", String.class, qp), "%" + qv);
                    break;
                case STARTS_WITH_MATCH_MODE:
                default:
                    qq = cb.like(cb.function("tostring", String.class, qp), qv + "%");
            }
        } else if (Date.class.equals(c)) {
            switch (af.getMatchMode()) {
                case GREATER_THAN_EQUALS_MODE:
                    qq = cb.greaterThanOrEqualTo(qp, (Date) qv);
                    break;
                case GREATER_THAN_MODE:
                    qq = cb.greaterThan(qp, (Date) qv);
                    break;
                case LESS_THAN_EQUALS_MODE:
                    qq = cb.lessThanOrEqualTo(qp, (Date) qv);
                    break;
                case LESS_THAN_MODE:
                    qq = cb.lessThan(qp, (Date) qv);
                    break;
                default:
                    qq = cb.equal(qp, qv);
            }
        } else if (Boolean.class.equals(c)) {
            switch (af.getMatchMode()) {
                case EQUALS_MODE:
                    qq = cb.equal(qp, qv);
                    break;
                default:
                    qq = cb.equal(qp, qv);
            }
        } else {
            throw new UnsupportedFilterColumnTypeException();
        }
        return qq;
    }

    private static final class UnsupportedFilterColumnTypeException extends Exception {
    }

    protected javax.persistence.criteria.Order[] getOrder(
            String column,
            Boolean asc) {

        PathInfo e = getPath(getRoot(entityClass), entityClass, column);
        if (e == null) {
            return getDefaultOrder();
        }

        if (asc == null || asc.equals(Boolean.TRUE)) {
            return new Order[]{cb.asc(e.p)};
        }
        return new Order[]{cb.desc(e.p)};
    }

    protected javax.persistence.criteria.Order[] getDefaultOrder() {
        return new Order[]{cb.asc(getDefaultSortColumnPath())};
    }

    protected Path getDefaultSortColumnPath() {
        Class c = entityClass;
        Path m = getRoot(c);
        ManagedType mt = em.getMetamodel().managedType(c);
        if (mt instanceof EntityType) {
            EntityType et = em.getMetamodel().entity(c);
            if (et.hasSingleIdAttribute()) {
                return m.get(et.getId(getIDClass()));
            } else {
                Set<SingularAttribute> idClassAttributes = et.getIdClassAttributes();
                SingularAttribute[] ta = idClassAttributes.toArray(new SingularAttribute[0]);
                return m.get(ta[0]);
            }
        } else {
            return null;
        }
    }

    protected PathInfo getPath(Path m, Class c, String column) {
        if (column == null || column.isEmpty()) {
            return null;
        }
        Path p = m;
        Join j = null;
        Class ccc = c;
        while (column.contains(".")) {
            String cn = column.substring(0, column.indexOf("."));
            column = column.substring(column.indexOf(".") + 1);
            Root r = getRoot(ccc);
            Attribute attr = em.getMetamodel().managedType(ccc).getAttribute(cn);
            ccc = attr.getJavaType();
            if (Attribute.PersistentAttributeType.EMBEDDED.equals(attr.getPersistentAttributeType())) {
                p = r.get(cn);
                continue;
            }
            p = j = (j == null ? r : j).join(cn, JoinType.LEFT);
            if (p == null) {
                return null;
            }
        }
        try {
            return new PathInfo(ccc, p.get(column), column);
        } catch (java.lang.IllegalArgumentException ex) {
            Logger.getLogger(QueryBuilder.class.getName()).info("Illegal sort column " + column + " for entity " + entityClass.getSimpleName());
            return null;
        }
    }

    protected Root getRoot(Class c) {
        if (!roots.containsKey(c)) {
            roots.put(c, cq.from(c));
        }
        return roots.get(c);
    }

    private static final class PathInfo {

        Class c;
        Path p;
        String n;

        public PathInfo(Class c, Path p, String n) {
            this.c = c;
            this.p = p;
            this.n = n;
        }

    }

}
