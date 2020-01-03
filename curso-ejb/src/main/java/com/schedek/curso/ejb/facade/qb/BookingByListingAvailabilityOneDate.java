/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade.qb;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.ejb.util.CalendarData;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Razzier
 */
public class BookingByListingAvailabilityOneDate extends QueryBuilder {
    private Date date;
    private Listing l;

    public BookingByListingAvailabilityOneDate(EntityManager em, Class entityClass, Date date, Listing l) {
        super(em, entityClass);
        this.date = date;
        this.l = l;
    }

    @Override
    protected Predicate getPredicate() {
        Root<Booking> rcvp = getRoot(entityClass);
        Predicate p = super.getPredicate();
        if (date != null && l != null) {
            return cb.and(p, cb.equal(rcvp.get("listing"), l),
                    cb.lessThanOrEqualTo(rcvp.<Date>get("startDate"), CalendarData.getDateOnly(date)),
                    cb.greaterThanOrEqualTo(rcvp.<Date>get("endDate"), CalendarData.getDateOnly(date)),
                    cb.notEqual(rcvp.get("canceled"), true)
            );
        }
        return cb.conjunction();

    }

}
