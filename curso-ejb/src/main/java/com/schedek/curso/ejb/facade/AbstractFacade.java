package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.facade.util.QueryBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class AbstractFacade<T> {

	private Class<T> entityClass;

	public AbstractFacade(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected abstract EntityManager getEntityManager();

	public void create(T entity) {
		getEntityManager().persist(entity);
	}

	public void edit(T entity) {
		getEntityManager().merge(entity);
	}

	public void remove(T entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}

	public List<T> findAll() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return getEntityManager().createQuery(cq).getResultList();
	}

	protected List<T> findAllSorted(String column) {
		return findAllSorted(column, true);
	}

	protected List<T> findAllSorted(String column, boolean asc) {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		cq.select(cq.from(getEntityClass()));
		Root<T> country = cq.from(getEntityClass());
		Path p = country.get(column);
		cq.orderBy(asc ? cb.asc(p) : cb.desc(p));
		return getEntityManager().createQuery(cq).getResultList();
	}

	public List<T> findRange(int[] range) {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		return q.getResultList();
	}

	public int count() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(getEntityManager().getCriteriaBuilder().count(rt));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

	public Object getID(T e) {
		return getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(e);
	}

	public Class getIDClass() {
		return getIDClass(entityClass);
	}

	protected Class getIDClass(Class c) {
		return getEntityManager().getMetamodel().entity(c).getIdType().getJavaType();
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public QueryBuilder<T> getDefaultQueryBuilder() {
		return new QueryBuilder<>(getEntityManager(), entityClass);
	}

	protected Predicate getTextSearchPredicate(CriteriaBuilder cb, Expression<String> se, String name) {
		return getTextSearchPredicate(cb, new Expression[]{se}, name);
	}

	protected Predicate getTextSearchPredicate(CriteriaBuilder cb, Expression<String>[] sea, String name) {
		List<Predicate> pl = new ArrayList<Predicate>();
		for (String pe : name.toLowerCase().split(" ")) {
			List<Predicate> p2 = new ArrayList<Predicate>();
			for (Expression<String> se : sea) {
				p2.add(cb.like(cb.lower(se), "%" + pe + "%"));
			}

			pl.add(cb.or(p2.toArray(new Predicate[]{})));
		}

		return cb.and(pl.toArray(new Predicate[]{}));
	}

	public void flush() {
		getEntityManager().flush();
	}

	protected <T> List<SelectItem> getFilterOptions(String column) {
		return getFilterOptions(String.class, column);
	}

	protected <Q> List<SelectItem> getFilterOptions(Class c, String column) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Q> cq = cb.createQuery(c);
		Root r = cq.from(getEntityClass());
		Expression x = r.get(column).as(c);
		if (String.class.equals(c)) {
			x = cb.lower(x);
		}
		cq.select(r.get(column).as(c)).orderBy(cb.asc(x)).distinct(true);
		return getFilterOptionsList(c, column, em.createQuery(cq).getResultList());
	}

	protected <Q> List<SelectItem> getFilterOptionsList(Class c, String column, List<Q> l) {
		List<SelectItem> ils = new ArrayList<SelectItem>();
		//	ils.add(new SelectItem("", "--", "", false, true, true));
		for (Q re : l) {
			if (re == null) {
				continue;
			}
			ils.add(new SelectItem(re, re.toString()));
		}
		return ils;

	}

	protected Long nextNumber(Date date, long type) {
		Long numberPrefix = getNumberPrefix(date, type);
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		Root prod = cq.from(entityClass
		);
		cq.where(cb.and(cb.greaterThan(prod.<Long>get("number"), numberPrefix), cb.lessThanOrEqualTo(prod.<Long>get("number"), numberPrefix + 999)));
		cq.select(cb.max(prod.<Long>get("number")));

		String s = (String) getEntityManager().createQuery(cq).getSingleResult();
		Long o;
		if (s == null) {
			o = numberPrefix;
		} else {
			o = Long.parseLong(s);

		}

		return o + 1;
	}

	protected static Long getNumberPrefix(Date date, long type) {
		Calendar calendarDate = Calendar.getInstance();
		calendarDate.setTime(date);
		long year = calendarDate.get(Calendar.YEAR);
		long month = calendarDate.get(Calendar.MONTH) + 1;
		return new Long(+type * 1000000000L + year * 100000L + month * 1000L);

	}

	public T getSingleResultOrNull(TypedQuery<T> query) {
		try {
			return query.setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
