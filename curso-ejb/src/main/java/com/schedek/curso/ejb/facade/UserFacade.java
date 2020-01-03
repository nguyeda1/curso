package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Group;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Root
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @EJB
    GroupFacade gf;

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    public User findCLF() {
        return findOneByUsername("CLF");
    }

    public User findPraguedays() {
        return findOneByUsername("praguedays");
    }

    public List<User> asyncSearch(String query) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<User> criteriaQuery = criteriaBuilder
                .createQuery(User.class);
        Root<User> c = criteriaQuery.from(User.class);

        String q = (query + "%").toLowerCase();
        Predicate or = criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(c.<String>get("firstname")), q),
                criteriaBuilder.like(criteriaBuilder.lower(c.<String>get("lastname")), q),
                criteriaBuilder.like(criteriaBuilder.lower(c.<String>get("username")), q)
        );

        CriteriaQuery<User> select = criteriaQuery.select(c);
        select.where(or);
        TypedQuery<User> typedQuery = em.createQuery(select);

        return typedQuery.getResultList();
    }

    public List<User> findByPage(int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<User> criteriaQuery = criteriaBuilder
                .createQuery(User.class);
        Root<User> c = criteriaQuery.from(User.class);

        criteriaQuery.orderBy(criteriaBuilder.asc(c.get("lastname")));

        CriteriaQuery<User> select = criteriaQuery.select(c);

        TypedQuery<User> typedQuery = em.createQuery(select);

        typedQuery.setFirstResult((pageNumber) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    public Long getUserCount() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder
                .createQuery(User.class);

        Root<User> a = criteriaQuery.from(User.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);

        countQuery.select(criteriaBuilder
                .count(a));
        return em.createQuery(countQuery)
                .getSingleResult();
    }

    public List<User> findByName(String name, int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<User> r = cq.from(User.class);
        cq.where(getTextSearchPredicate(cb, r.get("username").as(String.class), name));
        cq.select(r);
        return em.createQuery(cq).setMaxResults(limit).getResultList();
    }

    public List<User> findByNameOnlyActive(String name, int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<User> r = cq.from(User.class);
        cq.where(getTextSearchPredicate(cb, r.get("username").as(String.class), name), cb.isNotEmpty(r.<List>get("groups")));
        cq.select(r);
        return em.createQuery(cq).setMaxResults(limit).getResultList();
    }

    public User findOneByUsername(String username) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Root<User> prod = cq.from(User.class);
        cq.where(cb.equal(prod.get("username"), username));
        cq.select(prod);
        List<User> res = getEntityManager().createQuery(cq).getResultList();
        if (res.size() == 1) {
            return res.get(0);
        }
        return null;
    }


    public List<User> findAdmins() {
        return qbFilterActive(Arrays.asList(new Group[]{gf.findByName("admin")})).loadAll();
    }
    public QueryBuilder<User> qbFilterAll(final List<Group> g) {
        return qbFilter(g, false);
    }

    public QueryBuilder<User> qbFilterActive(final List<Group> g) {
        return qbFilter(g, true);
    }

    public List<User> findAllWithOutAdmin() {
        return qbAllWithOutAdmin(true).loadAll();

    }
       
    public QueryBuilder<User> qbAllWithOutAdmin( final boolean activeOnly) {
        return new QueryBuilder<User>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<User> rcvp = getRoot(getEntityClass());
                Predicate p = cb.conjunction();
                        p = cb.and(p, cb.isNotMember(gf.findByName("admin"), rcvp.<List>get("groups")));
                if (activeOnly) {
                    p = cb.and(p, cb.isNotEmpty(rcvp.<List>get("groups")));
                }
                return cb.and(p, super.getPredicate());
            }

            @Override
            protected Order[] getDefaultOrder() {
                return new Order[]{cb.asc(cb.lower(getRoot(getEntityClass()).<String>get("username")))};
            }

        };
    }
    
    public QueryBuilder<User> qbFilter(final List<Group> g, final boolean activeOnly) {
        return new QueryBuilder<User>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<User> rcvp = getRoot(getEntityClass());
                Predicate p = cb.conjunction();
                if (g != null && !g.isEmpty()) {
                    p = cb.disjunction();
                    for (Group gr : g) {
                        p = cb.or(p, cb.isMember(gr, rcvp.<List>get("groups")));
                    }
                }
                if (activeOnly) {
                    p = cb.and(p, cb.isNotEmpty(rcvp.<List>get("groups")));
                }
                return cb.and(p, super.getPredicate());
            }

            @Override
            protected Order[] getDefaultOrder() {
                return new Order[]{cb.asc(cb.lower(getRoot(getEntityClass()).<String>get("username")))};
            }

        };
    }

    public String createUsername(User user) {
        String lastName = user.getLastname().length() < 5 ? user.getLastname() : user.getLastname().substring(0, 5);
        String firstName = user.getFirstname().length() < 3 ? user.getFirstname() : user.getFirstname().substring(0, 3);
        String name = lastName + firstName;
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        name = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        int order = 1;
        while (findByName(name, 9).size() > 0) {
            name = name.length() < 7 ? name : name.substring(0, 7);
            name = name.concat(String.valueOf(order));
            order++;
        }
        return name.trim();
    }

    public List<User> findAllSorted(String columnName) {
        return super.findAllSorted(columnName);
    }
}
