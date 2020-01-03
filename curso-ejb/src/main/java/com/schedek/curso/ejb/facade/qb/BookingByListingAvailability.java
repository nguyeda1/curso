/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade.qb;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.facade.util.QueryBuilder;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Razzier
 */
public class BookingByListingAvailability extends QueryBuilder {

    private Listing l;
    private List<Listing> ls;
    private Date start;
    private Date end;

    public BookingByListingAvailability(EntityManager em, Class entityClass, Date start, Date end, Listing l) {
        super(em, entityClass);
        this.l = l;
        this.start = start;
        this.end = end;
    }

    public BookingByListingAvailability(EntityManager em, Class entityClass, Date start, Date end, List<Listing> ls) {
        super(em, entityClass);
        this.ls = ls;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Predicate getPredicate() {
        Root<Booking> rcvp = getRoot(entityClass);
        Predicate p = super.getPredicate();
        if (start != null && end != null && (l != null || ls != null)) {
            Predicate x = cb.disjunction();
            if (l != null) {
                x = cb.equal(rcvp.get("listing"), l);
            } else if (ls != null) {
                for (Listing listing : ls) {
                    x = cb.or(x, cb.equal(rcvp.get("listing"), listing));
                }
            }
            return cb.and(p, x,
                    cb.or(
                            cb.and(
                                    cb.lessThanOrEqualTo(rcvp.<Date>get("startDate"), start),
                                    cb.greaterThanOrEqualTo(rcvp.<Date>get("endDate"), end)
                            ),
                            cb.and(
                                    cb.greaterThanOrEqualTo(rcvp.<Date>get("startDate"), start),
                                    cb.lessThan(rcvp.<Date>get("startDate"), end)
                            ),
                            cb.and(
                                    cb.greaterThan(rcvp.<Date>get("endDate"), start),
                                    cb.lessThanOrEqualTo(rcvp.<Date>get("endDate"), end)
                            )
                    ),
                    cb.equal(rcvp.get("canceled"), false)
            );
        }
        return cb.conjunction();

    }

}
