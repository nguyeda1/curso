package com.schedek.curso.ejb.facade.qb;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.BookingState;
import com.schedek.curso.ejb.facade.BookingFacade;
import com.schedek.curso.ejb.facade.booking.QueryMode;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BookingActiveQueryBuilder extends QueryBuilder<Booking> {

    protected Listing s;
    protected List<BookingState> states;
    protected Date minDate;
    protected Date maxDate;
    protected QueryMode qm;
    protected User km;
    protected Boolean canceled;
    protected Boolean parking;
    protected Boolean orderBySupplyDate;

    public BookingActiveQueryBuilder(EntityManager em, Class entityClass, Listing s, List<BookingState> states, Date minDate, Date maxDate, QueryMode qm, User km, Boolean canceled, Boolean parking, Boolean orderBySupplyDate) {
        super(em, entityClass);
        this.s = s;
        this.states = states;
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.qm = qm;
        this.km = km;
        this.canceled = canceled;
        this.parking = parking;
        this.orderBySupplyDate = orderBySupplyDate;
    }

    @Override
    protected Predicate getPredicate() {
        Root<Booking> rcvp = getRoot(entityClass);
        Predicate p = super.getPredicate();
        if (s != null) {
            p = cb.and(p, cb.equal(rcvp.get("listing"), s));
        }
        if (km != null) {
            if (QueryMode.START.equals(qm)) {
                p = cb.and(p, cb.equal(rcvp.get("keymaster"), km));
            } else {
                p = cb.and(p, cb.equal(rcvp.get("keymaster_cleaning"), km));
            }
        }

        p = cb.and(p, minmaxDatePredicate());
        if (Boolean.FALSE.equals(canceled)) {
            p = cb.and(p, cb.or(cb.equal(rcvp.get("canceled"), canceled), cb.isNull(rcvp.get("canceled"))));
        }
        if(Boolean.TRUE.equals(canceled)){
            p = cb.and(p, cb.equal(rcvp.get("canceled"), canceled));
        }

        if (Boolean.FALSE.equals(parking)) {
            p = cb.and(p, cb.or(cb.equal(rcvp.get("parking"), parking), cb.isNull(rcvp.get("parking"))));
        }
        if(Boolean.TRUE.equals(parking)){
            p = cb.and(p, cb.equal(rcvp.get("parking"), parking));
        }
        
        if (states != null) {
            Predicate q = cb.disjunction();
            for (BookingState st : states) {
                q = cb.or(q, cb.equal(rcvp.get("state"), st));
            }
            if (QueryMode.START_ALERT.equals(qm)) {
                q = cb.or(q, cb.equal(rcvp.get("ciAlert"), true));
            }
            p = cb.and(p, q);
        }
        return p;
    }

    protected Predicate minmaxDatePredicate() {
        Root<Booking> rcvp = getRoot(entityClass);
        Predicate d = cb.conjunction();
        if (maxDate != null) {
            d = cb.and(d, cb.lessThanOrEqualTo(rcvp.<Date>get(qm.getDateColumn()), maxDate), cb.isNotNull(rcvp.get(qm.getDateColumn())));
        }
        if (minDate != null) {
            d = cb.and(d, cb.greaterThanOrEqualTo(rcvp.<Date>get(qm.getDateColumn()), minDate), cb.isNotNull(rcvp.get(qm.getDateColumn())));
        }
        return d;
    }

    @Override
    protected Order[] getDefaultOrder() {
        Root<Booking> rcvp = getRoot(entityClass);
        if(orderBySupplyDate!=null && orderBySupplyDate) return new Order[]{cb.asc(rcvp.get("dateSupply")), cb.desc(rcvp.get("supplyPriority"))};
        
        return new Order[]{cb.asc(rcvp.get(qm.getDateColumn())),
            cb.asc(rcvp.get(qm.getTimeColumn()))};
    }

}
