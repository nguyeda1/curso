/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.cases;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseFeedback;
import com.schedek.curso.ejb.enums.CasePriority;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.xmladapter.DateCzAdapter;
import com.schedek.curso.taskmanager.app.dto.booking.BookingWrapper;
import com.schedek.curso.taskmanager.app.dto.listing.ListingWrapper;
import com.schedek.curso.taskmanager.app.dto.user.UserWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tomáš
 */
@XmlRootElement
public class CaseWrapper implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Date createdOn;
    private UserWrapper createdBy;
    private Date finishedOn;
    private Date deadline;
    private BookingWrapper booking;
    private ListingWrapper listing;
    private UserWrapper owner;
    private UserWrapper assignee;
    private CaseState caseState;
    private CasePriority priority;
    private List<UserWrapper> followers;

    public CaseWrapper() {
    }

    public CaseWrapper(Case c) {
        if (c != null) {
            this.id = c.getId();
            this.name = c.getName();
            this.description = c.getDescription();
            this.createdOn = c.getCreatedOn();
            this.createdBy = c.getCreatedBy() != null ? new UserWrapper(c.getCreatedBy()) : null;
            this.finishedOn = c.getFinishedOn();
            this.deadline = c.getDeadline();
            this.booking = c.getBooking() != null ? new BookingWrapper(c.getBooking()) : null;
            this.listing = c.getListing() != null ? new ListingWrapper(c.getListing()) : null;
            this.owner = c.getOwner() != null ? new UserWrapper(c.getOwner()) : null;
            this.assignee = c.getAssignee() != null ? new UserWrapper(c.getAssignee()) : null;
            this.caseState = c.getCaseState();
            this.priority = c.getPriority();
            this.followers = c.getFollowers().stream().map(f -> {
                return new UserWrapper(f);
            }).collect(Collectors.toList());
        }
    }

    @XmlElement
    public Long getId() {
        return id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public Date getCreatedOn() {
        return createdOn;
    }

    @XmlElement
    public UserWrapper getCreatedBy() {
        return createdBy;
    }

    @XmlElement
    public Date getFinishedOn() {
        return finishedOn;
    }

    @XmlElement
    public Date getDeadline() {
        return deadline;
    }

    @XmlElement
    public BookingWrapper getBooking() {
        return booking;
    }

    @XmlElement
    public ListingWrapper getListing() {
        return listing;
    }

    @XmlElement
    public UserWrapper getOwner() {
        return owner;
    }

    @XmlElement
    public CaseState getCaseState() {
        return caseState;
    }

    @XmlElement
    public CasePriority getPriority() {
        return priority;
    }

    @XmlElement
    public UserWrapper getAssignee() {
        return assignee;
    }

    public List<UserWrapper> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UserWrapper> followers) {
        this.followers = followers;
    }

    public void setAssignee(UserWrapper assignee) {
        this.assignee = assignee;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedBy(UserWrapper createdBy) {
        this.createdBy = createdBy;
    }

    public void setFinishedOn(Date finishedOn) {
        this.finishedOn = finishedOn;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setBooking(BookingWrapper booking) {
        this.booking = booking;
    }

    public void setListing(ListingWrapper listing) {
        this.listing = listing;
    }

    public void setOwner(UserWrapper owner) {
        this.owner = owner;
    }

    public void setCaseState(CaseState caseState) {
        this.caseState = caseState;
    }

    public void setPriority(CasePriority priority) {
        this.priority = priority;
    }
}
