package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.AppEJB;
import com.schedek.curso.ejb.entities.*;
import com.schedek.curso.ejb.enums.BookingState;
import com.schedek.curso.ejb.enums.BookingType;
import com.schedek.curso.ejb.facade.booking.QueryMode;
import com.schedek.curso.ejb.facade.qb.BookingActiveQueryBuilder;
import com.schedek.curso.ejb.facade.qb.BookingByListingAvailability;
import com.schedek.curso.ejb.facade.qb.BookingByListingAvailabilityOneDate;
import com.schedek.curso.ejb.facade.qb.BookingByStateQueryBuilder;
import com.schedek.curso.ejb.facade.qb.BookingDayComparator;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.ejb.util.exceptions.BookingColisionException;
import com.schedek.curso.ejb.util.exceptions.BookingFacadeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
public class BookingFacade extends AbstractFacade<Booking> {

    @EJB
    ListingFacade lif;
    @EJB
    UserFacade uf;
    @Resource
    private SessionContext ctx;
    @EJB
    TaskFacade tf;
    @EJB
    AppEJB app;
    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Booking findByIdAndListing(Listing l, Long id) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Booking> criteriaQuery = criteriaBuilder
                .createQuery(Booking.class);
        Root<Booking> c = criteriaQuery.from(Booking.class);

        CriteriaQuery<Booking> select = criteriaQuery.select(c);
        if (l != null) {
            select.where(criteriaBuilder.equal(c.get("id"), id), criteriaBuilder.equal(c.get("listing"), l));
        } else {
            select.where(criteriaBuilder.equal(c.get("id"), id));
        }

        TypedQuery<Booking> typedQuery = em.createQuery(select);
        List<Booking> res = typedQuery.getResultList();
        if (res.size() == 1) {
            res.get(0);
        }
        return null;
    }

    public List<Booking> asyncSearch(Listing l, String query) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Booking> criteriaQuery = criteriaBuilder
                .createQuery(Booking.class);
        Root<Booking> c = criteriaQuery.from(Booking.class);

        String q = ("%" + query + "%").toLowerCase();
        Predicate or = criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(c.<String>get("guestName")), q)
        );

        CriteriaQuery<Booking> select = criteriaQuery.select(c);
        select.where(or, criteriaBuilder.equal(c.get("listing"), l));
        TypedQuery<Booking> typedQuery = em.createQuery(select);

        return typedQuery.getResultList();
    }

    public List<Booking> findByListingAndPage(Listing l, int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Booking> criteriaQuery = criteriaBuilder
                .createQuery(Booking.class);
        Root<Booking> c = criteriaQuery.from(Booking.class);

        criteriaQuery.orderBy(criteriaBuilder.desc(c.get("startDate")));

        CriteriaQuery<Booking> select = criteriaQuery.select(c);

        if (l != null) {
            select.where(criteriaBuilder.equal(c.get("listing"), l));
        }

        TypedQuery<Booking> typedQuery = em.createQuery(select);

        typedQuery.setFirstResult((pageNumber) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    public Long getBookingCountByListing(Listing c) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Booking> criteriaQuery = criteriaBuilder
                .createQuery(Booking.class);

        Root<Booking> a = criteriaQuery.from(Booking.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);

        countQuery.select(criteriaBuilder
                .count(a));

        if (c != null) {
            countQuery.where(criteriaBuilder.equal(a.get("listing"), c));
        }

        return em.createQuery(countQuery)
                .getSingleResult();
    }

    private void cancelBooking(Booking b, User u) {
        if (b.getState() == null || b.getState().isGuestIn() || b.getState().isGuestOut() || b.getState().isReview() || b.getState().isAccounting() || b.getState().isFinish()) {
            throw new EJBException("Unsupported booking state for cancel");
        }
        b.setCanceled(true);
        b.setCanceledBy(u);
    }

    @Override
    public void create(Booking entity) {
        Objects.requireNonNull(entity.getStartDate());
        Objects.requireNonNull(entity.getEndDate());
        Objects.requireNonNull(entity.getListing());

        List<Booking> bookings = qbListingAvailability(entity.getListing(), entity.getStartDate(), entity.getEndDate()).loadAll();
        if (bookings.isEmpty()) {
            super.create(entity);
        } else {
            throw new BookingColisionException("Booking colides with following bookings: " + getHumanReadableColisionIds(bookings, entity));
        }
    }

    public String addText(String text) {
        if (text != null) {
            return " (" + text + ")";
        }
        return "";

    }

    @Override
    public void edit(Booking entity) {
        Booking b = find(entity.getId());

        Objects.requireNonNull(entity.getStartDate());
        Objects.requireNonNull(entity.getEndDate());
        Objects.requireNonNull(entity.getListing());

        List<Booking> bookings = qbListingAvailability(entity.getListing(), entity.getStartDate(), entity.getEndDate()).loadAll();
        if (b.isCanceled() && !entity.isCanceled()) {
            throw new BookingFacadeException("Uncancelling booking is not allowed");
        }

        if ((bookings.size() == 1 && bookings.get(0).getId().equals(entity.getId())) || bookings.isEmpty() || entity.isCanceled()) {
            super.edit(entity);

        } else {
            throw new BookingColisionException("Booking colides with following bookings: " + getHumanReadableColisionIds(bookings, entity));
        }
    }

    public void detach(Booking b) {
        em.detach(b);
    }

    public void cancel(Booking b, User u) {
        if (!b.isCanceled()) {
            Logger.getLogger(BookingFacade.class.getName()).log(Level.INFO, "Canceling booking: {0}", b.getId());
            detach(b);
            cancelBooking(b, u);
            b.setState(BookingState.REVIEW);
            edit(b);
            List<Task> t = tf.findTaskByLockboxBooking(b).loadAll();
            if (t.size() > 0) {
                tf.remove(t.get(0));
            }
        }
    }

    private String getHumanReadableColisionIds(List<Booking> bookings, Booking entity) {
        String ids = "";
        for (Booking booking : bookings) {
            if (entity == null || !Objects.equals(entity.getId(), booking.getId())) {
                ids += booking.getId() + ", ";
            }
        }
        ids = ids.substring(0, ids.length() - 2);
        return ids;
    }

    public QueryBuilder<Booking> qbBookingInInterval(final Date start, final Date end, final Listing listing) {
        return new QueryBuilder<Booking>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Booking> rcvp = getRoot(getEntityClass());
                Predicate p = super.getPredicate();
                if (listing != null) {
                    p = cb.and(p, cb.equal(rcvp.get("listing"), listing));
                }
                return cb.and(
                        p,
                        cb.equal(rcvp.get("canceled"), false),
                        cb.lessThanOrEqualTo(rcvp.<Date>get("endDate"), end),
                        cb.greaterThanOrEqualTo(rcvp.<Date>get("startDate"), start)
                );
            }

            @Override
            protected Order[] getOrder(String column, Boolean asc) {
                return new Order[]{cb.desc(getRoot(entityClass).get("endDate"))};
            }

        };
    }

    public QueryBuilder<Booking> qbByState(final Listing s, final BookingState bs, Boolean invoiced) {
        return new BookingByStateQueryBuilder(em, getEntityClass(), s, bs, invoiced);
    }

    public List<Booking> findByGuestName(String filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Booking> r = cq.from(Booking.class);
        BookingState[] states = new BookingState[]{BookingState.GUEST_OUT,
            BookingState.REVIEW,
            BookingState.ACCOUNTING,
            BookingState.FINISH};
        Predicate q = cb.disjunction();
        for (BookingState st : states) {
            q = cb.or(q, cb.equal(r.get("state"), st));
        }
        cq.where(cb.and(cb.like(r.<String>get("guestName"), filter + "%"), q));
        cq.orderBy(cb.desc(r.get("endDate")));
        cq.select(r);
        return em.createQuery(cq).getResultList();
    }





    public QueryBuilder<Booking> qbFilterAll(final Listing s, final List<BookingState> states, final boolean reviewsFilter) {
        return qbFilterActive(s, states, reviewsFilter, null);
    }

    private QueryBuilder<Booking> qbFilterActive(final Listing s, final List<BookingState> states, final boolean reviewsFilter, Boolean canceled) {

        return new BookingActiveQueryBuilder(em, getEntityClass(), s, states, null, null, QueryMode.START, null, canceled, null, reviewsFilter);

    }

    public QueryBuilder<Booking> qbListingAvailability(final Listing l, final Date start, final Date end) {
        return new BookingByListingAvailability(em, getEntityClass(), start, end, l);
    }

    public QueryBuilder<Booking> qbListingsAvailability(final List<Listing> l, final Date start, final Date end) {
        return new BookingByListingAvailability(em, getEntityClass(), start, end, l);
    }

    public QueryBuilder<Booking> qbListingAvailability(final Listing l, final Date date) {
        return new BookingByListingAvailabilityOneDate(em, getEntityClass(), date, l);
    }


    public BookingFacade() {
        super(Booking.class
        );
    }

    public Booking factoryMethod(Listing l) {
        Booking b = new Booking();
        b.setListing(l);

        //set default cleaning supplier
        return b;
    }

    public void refresh(Booking b) {
        em.refresh(b);
    }


    public List<Booking> getBookingsWithSameGuestName(Booking b) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Booking> cq = cb.createQuery(Booking.class);
        Root<Booking> r = cq.from(Booking.class);
        cq.where(cb.equal(r.get("guestName"), b.getGuestName()));
        cq.select(r);
        return em.createQuery(cq).getResultList();
    }

}
