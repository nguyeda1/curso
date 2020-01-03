/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.entities;

import com.schedek.curso.ejb.enums.CaseOption;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.enums.CasePriority;
import com.schedek.curso.ejb.view.ViewCaseStats;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dan Nguyen
 */
@Entity
@Table(name = "cases")
public class Case implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedOn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;
    @ManyToOne
    private Listing listing;
    @ManyToOne
    private Booking booking;
    @Enumerated(EnumType.STRING)
    private CaseState caseState = CaseState.NEW;
    @Enumerated(EnumType.STRING)
    private CaseOption approvedWithClient = CaseOption.NO;
    @Enumerated(EnumType.STRING)
    private CaseOption guestInformed = CaseOption.NO;
    @ManyToOne
    private User createdBy;
    @ManyToOne
    private User finishedBy;
    @ManyToOne
    private User owner;
    @ManyToOne
    private User previousOwner;
    @Enumerated(EnumType.STRING)
    private CasePriority priority = CasePriority.MEDIUM;
    @OneToOne(mappedBy = "casex", fetch = FetchType.LAZY)
    private ViewCaseStats stats;
    @ManyToOne
    private User assignee;
    @ManyToOne
    private User previousAssignee;
    @Temporal(TemporalType.TIMESTAMP)
    private Date assigned;
    @ManyToMany
    @JoinTable(name = "case_casetag", joinColumns = @JoinColumn(name = "case_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<CaseTag> tags = new ArrayList();
    @ManyToMany
    @JoinTable(name = "case_followers", joinColumns = @JoinColumn(name = "case_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> followers = new ArrayList();
    @ManyToOne
    private Group role;
    private boolean template =  false;
    

    public Case() {
    }

    public Case(String name) {
        this.name = name;
    }

    public Group getRole() {
        return role;
    }

    public void setRole(Group role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public Date getAssigned() {
        return assigned;
    }

    public void setAssigned(Date assigned) {
        this.assigned = assigned;
    }

    public User getPreviousOwner() {
        return previousOwner;
    }

    public void setPreviousOwner(User previousOwner) {
        this.previousOwner = previousOwner;
    }

    public User getPreviousAssignee() {
        return previousAssignee;
    }

    public void setPreviousAssignee(User previousAssignee) {
        this.previousAssignee = previousAssignee;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }
    
    public ViewCaseStats getStats() {
        return stats;
    }

    public void setStats(ViewCaseStats stats) {
        this.stats = stats;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date created) {
        this.createdOn = created;
    }

    public Date getFinishedOn() {
        return finishedOn;
    }

    public void setFinishedOn(Date finishedOn) {
        this.finishedOn = finishedOn;
    }

    public User getFinishedBy() {
        return finishedBy;
    }

    public void setFinishedBy(User finishedBy) {
        this.finishedBy = finishedBy;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public CaseState getCaseState() {
        return caseState;
    }

    public void setCaseState(CaseState caseState) {
        this.caseState = caseState;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public CasePriority getPriority() {
        return priority;
    }

    public void setPriority(CasePriority priority) {
        this.priority = priority;
    }

    public List<CaseTag> getTags() {
        return tags;
    }

    public void setTags(List<CaseTag> tags) {
        this.tags = tags;
    }

    public CaseOption getApprovedWithClient() {
        return approvedWithClient;
    }

    public void setApprovedWithClient(CaseOption approvedWithClient) {
        this.approvedWithClient = approvedWithClient;
    }

    public CaseOption getGuestInformed() {
        return guestInformed;
    }

    public void setGuestInformed(CaseOption guestInformed) {
        this.guestInformed = guestInformed;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public String getLabel() {
        return "#" + this.id + " " + this.name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Case other = (Case) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
