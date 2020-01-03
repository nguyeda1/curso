/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade.booking;

import com.schedek.curso.ejb.enums.BookingState;

/**
 *
 * @author Razzier
 */
public enum EventQueryMode {
    NEW, APPROVED, GUEST_IN, GUEST_OUT, REVIEW, ACCOUNTING;

    public BookingState[] getStates() {
        switch (this) {
            case NEW:
                return new BookingState[]{BookingState.NEW};
            case APPROVED:
                return new BookingState[]{BookingState.APPROVED, BookingState.ASSIGNED};
            case GUEST_IN:
                return new BookingState[]{BookingState.GUEST_IN};
            case GUEST_OUT:
                return new BookingState[]{BookingState.GUEST_OUT};
            case REVIEW:
                return new BookingState[]{BookingState.REVIEW};
            case ACCOUNTING:
                return new BookingState[]{BookingState.ACCOUNTING};
            default:
                throw new AssertionError();
        }
    }

    public String getNiceName() {
        switch (this) {
            case NEW:
                return "New";
            case APPROVED:
                return "Approved";
            case GUEST_IN:
                return "Guest in";
            case GUEST_OUT:
                return "Guest out";
            case REVIEW:
                return "Review";
            case ACCOUNTING:
                return "Accounting";
            default:
                throw new AssertionError();
        }
    }

    public boolean isNew() {
        return equals(NEW);
    }

    public boolean isApproved() {
        return equals(APPROVED);
    }

    public boolean isGuestIn() {
        return equals(GUEST_IN);
    }

    public boolean isGuestOut() {
        return equals(GUEST_OUT);
    }

    public boolean isReview() {
        return equals(REVIEW);
    }

    public boolean isAccounting() {
        return equals(ACCOUNTING);
    }
    
}
