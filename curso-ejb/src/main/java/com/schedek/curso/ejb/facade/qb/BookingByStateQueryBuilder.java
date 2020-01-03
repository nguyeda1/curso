/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade.qb;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.enums.BookingState;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Razzier
 */
public class BookingByStateQueryBuilder extends QueryBuilder {

    private Listing s;
    private BookingState bs;
    private Boolean invoiced;

    public BookingByStateQueryBuilder(EntityManager em, Class entityClass, Listing s, BookingState bs, Boolean invoiced) {
        super(em, entityClass);
        this.s = s;
        this.bs = bs;
        this.invoiced = invoiced;
    }

    @Override
    protected Predicate getPredicate() {
        Root<Booking> rcvp = getRoot(entityClass);
        Predicate p = super.getPredicate();
        if (s != null) {
            p = cb.and(p, cb.equal(rcvp.get("listing"), s));
        }
        if (invoiced != null) {
            p = cb.and(p, cb.equal(rcvp.get("invoiced"), invoiced));
        }
        
        return cb.and(p, cb.equal(rcvp.get("state"), bs));

    }

    @Override
    protected Order[] getDefaultOrder() {
        Root<Booking> rcvp = getRoot(entityClass);
        return new Order[]{cb.asc(rcvp.get("startDate"))};
    }

}
