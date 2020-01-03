/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade.booking;

import com.schedek.curso.ejb.facade.booking.BookingEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Razzier
 */
public class BookingDay {
    
    private List<BookingEvent> events = new ArrayList<>();
    private Date date;
    private int index;

    public List<BookingEvent> getEvents() {
        return events;
    }

    public Date getDate() {
        return date;
    }

    public Date getDateEnd() {
        if(getDate()==null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(getDate());
        c.add(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public int getIndex() {
        return index;
    }

    public void setEvents(List<BookingEvent> events) {
        this.events = events;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    
    
}
