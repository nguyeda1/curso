/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade.booking;

import com.schedek.curso.ejb.entities.Booking;
import java.util.Date;

/**
 *
 * @author Razzier
 */
public class BookingEvent {
    
    private boolean start = false;
    private Date timestamp;
    private Booking booking;

    public boolean isStart() {
        return start;
    }

    public Booking getBooking() {
        return booking;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    
    
    
    
    
    
}
