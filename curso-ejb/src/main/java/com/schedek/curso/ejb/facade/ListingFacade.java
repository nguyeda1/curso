package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.*;
import com.schedek.curso.ejb.enums.BookingType;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.ejb.util.BigDec;
import com.schedek.curso.ejb.view.ListingBookingOverview;
import com.schedek.curso.ejb.view.ViewListingCurrentBooking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

@Stateless
public class ListingFacade extends AbstractFacade<Listing> {

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ListingFacade() {
        super(Listing.class);
    }

    public List<Listing> asyncSearch(String query) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Listing> criteriaQuery = criteriaBuilder
                .createQuery(Listing.class);
        Root<Listing> c = criteriaQuery.from(Listing.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(c.get("name")));

        String q = (query + "%").toLowerCase();
        Predicate or = criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(c.<String>get("name")), q),
                criteriaBuilder.like(criteriaBuilder.lower(c.<String>get("address")), q)
        );

        CriteriaQuery<Listing> select = criteriaQuery.select(c);

        select.where(or, criteriaBuilder.equal(c.get("disabled"), false));
        TypedQuery<Listing> typedQuery = em.createQuery(select);

        return typedQuery.getResultList();
    }

    public List<Listing> findByPage(int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Listing> criteriaQuery = criteriaBuilder
                .createQuery(Listing.class);
        Root<Listing> c = criteriaQuery.from(Listing.class);

        criteriaQuery.orderBy(criteriaBuilder.asc(c.get("name")));

        CriteriaQuery<Listing> select = criteriaQuery.select(c);

        select.where(criteriaBuilder.equal(c.get("disabled"), false));
        TypedQuery<Listing> typedQuery = em.createQuery(select);

        typedQuery.setFirstResult((pageNumber) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    public Long getListingCount() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Listing> criteriaQuery = criteriaBuilder
                .createQuery(Listing.class);

        Root<Listing> a = criteriaQuery.from(Listing.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);

        countQuery.where(criteriaBuilder.equal(a.get("disabled"), false));

        countQuery.select(criteriaBuilder
                .count(a));
        return em.createQuery(countQuery)
                .getSingleResult();
    }

    @Override
    public QueryBuilder<Listing> getDefaultQueryBuilder() {
        return new QueryBuilder(getEntityManager(), getEntityClass()) {
            @Override
            protected Path getDefaultSortColumnPath() {
                return getRoot(getEntityClass()).get("name");
            }

        };
    }

    @Override
    public void create(Listing entity) {
        super.create(entity);
    }

    public List<ListingBookingOverview> getListingBookingOverviewByMonth(Date month, Listing listing) {
        if (month != null && listing != null) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ListingBookingOverview> cq = cb.createQuery(ListingBookingOverview.class);
            Root<ListingBookingOverview> r = cq.from(ListingBookingOverview.class);
            cq.where(cb.and(cb.equal(r.get("month"), month), cb.equal(r.get("listing"), listing)));
            cq.select(r);
            return em.createQuery(cq).getResultList();
        }
        return null;

    }


    public QueryBuilder<Listing> qbActive(final Boolean active) {
        return new QueryBuilder<Listing>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Listing> rcvp = getRoot(getEntityClass());
                Predicate p = super.getPredicate();
                if (active != null) {
                    Predicate q = cb.equal(rcvp.get("disabled"), !active);
                    if (active) {
                        q = cb.or(q, cb.isNull(rcvp.get("disabled")));
                    }
                    p = cb.and(p, q);
                }
                return p;
            }

            @Override
            protected Path getDefaultSortColumnPath() {
                return getRoot(getEntityClass()).get("name");
            }
        };
    }

    public QueryBuilder<Listing> qbWorking() {
        return new QueryBuilder<Listing>(getEntityManager(), getEntityClass()) {
            
            @Override
            protected Predicate getPredicate() {
                Root<Listing> rcvp = getRoot(getEntityClass());
                Predicate p = super.getPredicate();
                Predicate q = cb.or(cb.equal(rcvp.get("disabled"), false), cb.isNull(rcvp.get("disabled")));
                Predicate r = cb.or(cb.isNull(rcvp.get("terminationDate")), cb.greaterThanOrEqualTo(rcvp.<Date>get("terminationDate"), new Date()));
                p = cb.and(p, q, cb.isNotNull(rcvp.get("startDate")), r);
                
                return p;
            }
        };
    }

    public Integer getMaxCapacity() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
        Root<Listing> r = cq.from(Listing.class);
        cq.select(cb.max(r.<Integer>get("capacity")));
        List<Integer> rl = em.createQuery(cq).getResultList();
        return rl.get(0);
    }
    public List<SelectItem> getCapacityOptions() {
        return getFilterOptions(Integer.class,
                "capacity");
    }

    @Override
    public List<Listing> findAll() {
        return super.findAllSorted("name");
    }

    public List<Listing> findActive() {
        return qbActive(true).loadAll();
    }

    public void refresh(Listing l) {
        if (l != null) {
            Listing listing = find(l.getId());
            em.refresh(listing);
        }
    }

    public QueryBuilder<ViewListingCurrentBooking> qbListingCurrentBooking() {
        return new QueryBuilder<ViewListingCurrentBooking>(getEntityManager(), ViewListingCurrentBooking.class) {
            @Override
            protected Predicate
            getPredicate() {
                Root<ViewListingCurrentBooking> rcvp = getRoot(ViewListingCurrentBooking.class);
                return cb.and(super.getPredicate(), cb.isNotNull(rcvp.get("currentBooking")));
            }
        };
    }


    public static class ListingEditEJBException extends EJBException {

        public ListingEditEJBException() {
            super();
        }

        public ListingEditEJBException(String message) {
            super(message);
        }

        public ListingEditEJBException(String message, Exception ex) {
            super(message, ex);
        }
    }
}
