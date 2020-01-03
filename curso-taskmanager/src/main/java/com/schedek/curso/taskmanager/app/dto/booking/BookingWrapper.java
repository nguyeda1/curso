/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.booking;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.taskmanager.app.dto.listing.ListingWrapper;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tomáš
 */
@XmlRootElement
public class BookingWrapper implements Serializable {

    private Long id;
    private String guestName;
    private Date startDate;
    private Date endDate;
    private ListingWrapper listing;
    private boolean canceled;

    public BookingWrapper() {
    }

    public BookingWrapper(Booking c) {
        if (c != null) {
            this.id = c.getId();
            this.guestName = c.getGuestName();
            this.startDate = c.getStartDate();
            this.endDate = c.getEndDate();
            this.listing = c.getListing() != null ? new ListingWrapper(c.getListing()) : null;
            this.canceled = c.isCanceled();
        }
    }

    @XmlElement
    public Long getId() {
        return id;
    }

    @XmlElement
    public String getGuestName() {
        return guestName;
    }

    @XmlElement
    public Date getStartDate() {
        return startDate;
    }

    @XmlElement
    public Date getEndDate() {
        return endDate;
    }

    @XmlElement
    public ListingWrapper getListing() {
        return listing;
    }

    @XmlElement
    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setListing(ListingWrapper listing) {
        this.listing = listing;
    }
}
