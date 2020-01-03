/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.entities;

import com.schedek.curso.ejb.enums.CaseOption;
import com.schedek.curso.ejb.enums.TaskFinishState;
import com.schedek.curso.ejb.enums.TaskPriority;
import com.schedek.curso.ejb.view.ViewTaskStats;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dan Nguyen
 */
@Entity
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob
    @Column
    private String description;
    @ManyToOne
    private Case cursoCase;
    @ManyToOne
    private Case previousCase;
    @ManyToOne
    private User assignee;
    @ManyToOne
    private Group assigneeRole;
    @ManyToOne
    private User previousAssignee;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.MEDIUM;
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean finished = false;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedOn;
    @ManyToOne
    private User finshedBy;
    @Temporal(TemporalType.DATE)
    private Date deadline;
    private Long deadlineInDays;
    private Long priorityOnScreen;
    @OneToOne(mappedBy = "task", fetch = FetchType.LAZY)
    private ViewTaskStats stats;
    @ManyToOne
    private User createdBy;
    @ManyToOne
    private User reviewedBy;
    @ManyToOne
    private Locality locality;
    @ManyToOne
    private Listing listing;
    @Temporal(TemporalType.DATE)
    private Date plannedOn;
    private Long plannedOnInDays;
    @Enumerated(EnumType.STRING)
    private CaseOption approvedWithClient = CaseOption.NO;
    @Enumerated(EnumType.STRING)
    private CaseOption guestInformed = CaseOption.NO;
    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<Subtask> subtask;
    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<TaskComment> comments;
    @Enumerated(EnumType.STRING)
    private TaskFinishState finishedState;
    @ManyToOne
    private Booking lockboxBooking;
    private BigDecimal estimatedPurchasePrice;
    private String purchasedItem;
    private String emailText;
    @Lob
    @Column
    private String problemText;
    boolean suitableForSupplyCheckDepartment = false;

    public Task() {
    }

    public Task(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSuitableForSupplyCheckDepartment() {
        return suitableForSupplyCheckDepartment;
    }

    public void setSuitableForSupplyCheckDepartment(boolean suitableForSupplyCheckDepartment) {
        this.suitableForSupplyCheckDepartment = suitableForSupplyCheckDepartment;
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

    public User getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(User reviewdBy) {
        this.reviewedBy = reviewdBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Long getPriorityOnScreen() {
        return priorityOnScreen;
    }

    public void setPriorityOnScreen(Long priorityOnScreen) {
        this.priorityOnScreen = priorityOnScreen;
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

    public Case getCursoCase() {
        return cursoCase;
    }

    public void setCursoCase(Case cursoCase) {
        this.cursoCase = cursoCase;
    }

    public Case getPreviousCase() {
        return previousCase;
    }

    public void setPreviousCase(Case previousCase) {
        this.previousCase = previousCase;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Group getAssigneeRole() {
        return assigneeRole;
    }

    public void setAssigneeRole(Group assigneeRole) {
        this.assigneeRole = assigneeRole;
    }

    public User getPreviousAssignee() {
        return previousAssignee;
    }

    public void setPreviousAssignee(User previousAssignee) {
        this.previousAssignee = previousAssignee;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getFinishedOn() {
        return finishedOn;
    }

    public void setFinishedOn(Date finishedOn) {
        this.finishedOn = finishedOn;
    }

    public User getFinshedBy() {
        return finshedBy;
    }

    public void setFinshedBy(User finshedBy) {
        this.finshedBy = finshedBy;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Long getDeadlineInDays() {
        return deadlineInDays;
    }

    public void setDeadlineInDays(Long deadlineInDays) {
        this.deadlineInDays = deadlineInDays;
    }

    public Listing getListing() {
        if (listing == null) {
            if (cursoCase != null && cursoCase.getListing() != null) {
                listing = cursoCase.getListing();
            }
        }
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Locality getLocality() {
        if (locality == null) {
            if (cursoCase != null && cursoCase.getListing() != null) {
                return cursoCase.getListing().getLocality();
            }
            if (listing != null) {
                return listing.getLocality();
            }
        }
        return locality;
    }

    public Locality getCustomLocality() {
        return locality;
    }

    public Date getPlannedOn() {
        return plannedOn;
    }

    public void setPlannedOn(Date plannedOn) {
        this.plannedOn = plannedOn;
    }
    
    public Long getPlannedOnInDays() {
        return plannedOnInDays;
    }

    public void setPlannedOnInDays(Long plannedOnInDays) {
        this.plannedOnInDays = plannedOnInDays;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public void setCustomLocality(Locality locality) {
        this.locality = locality;
    }

    public ViewTaskStats getStats() {
        return stats;
    }

    public void setStats(ViewTaskStats stats) {
        this.stats = stats;
    }

    public List<Subtask> getSubtask() {
        return subtask;
    }

    public void setSubtask(List<Subtask> subtask) {
        this.subtask = subtask;
    }

    public List<TaskComment> getComments() {
        return comments;
    }

    public void setComments(List<TaskComment> comments) {
        this.comments = comments;
    }

    public TaskFinishState getFinishedState() {
        return finishedState;
    }

    public void setFinishedState(TaskFinishState finishedState) {
        this.finishedState = finishedState;
    }

    public Booking getLockboxBooking() {
        return lockboxBooking;
    }

    public void setLockboxBooking(Booking lockboxBooking) {
        this.lockboxBooking = lockboxBooking;
    }

    public BigDecimal getEstimatedPurchasePrice() {
        return estimatedPurchasePrice;
    }

    public void setEstimatedPurchasePrice(BigDecimal estimatedPurchasePrice) {
        this.estimatedPurchasePrice = estimatedPurchasePrice;
    }

    public String getPurchasedItem() {
        return purchasedItem;
    }

    public void setPurchasedItem(String purchasedItem) {
        this.purchasedItem = purchasedItem;
    }

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }

    public String getProblemText() {
        return problemText;
    }

    public void setProblemText(String problemText) {
        this.problemText = problemText;
    }

    public boolean isPurchaseItemCheck() {
        if (getListing() != null && getEstimatedPurchasePrice() != null) {
            Integer x = 500;
            return getEstimatedPurchasePrice().compareTo(BigDecimal.valueOf(x)) >= 0;
        }
        return false;
    }

    public String getLabel() {
        return "#" + this.id + " " + this.name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Task other = (Task) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
