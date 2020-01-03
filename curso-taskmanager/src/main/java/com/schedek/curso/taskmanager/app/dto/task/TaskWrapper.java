/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.task;

import com.schedek.curso.taskmanager.app.dto.cases.CaseWrapper;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.enums.TaskFinishState;
import com.schedek.curso.ejb.enums.TaskPriority;
import com.schedek.curso.taskmanager.app.dto.listing.ListingWrapper;
import com.schedek.curso.taskmanager.app.dto.user.UserWrapper;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tomáš
 */
@XmlRootElement
public class TaskWrapper implements Serializable {

    private Long id;
    private String name;
    private String description;
    private TaskPriority priority;
    private UserWrapper assignee;
    private CaseWrapper cursoCase;
    private boolean checklistDone;
    private boolean finished;
    private Date deadline;
    private Date plannedOn;
    private String locality;
    private ListingWrapper listing;
    private Date finishedOn;
    private TaskFinishState finishedState;
    private Long priorityOnScreen;
    private String problemText;

    public TaskWrapper() {
    }

    public TaskWrapper(Task c) {
        init(c);
    }

    public TaskWrapper(Task c, boolean checklistDone) {
        init(c);
        this.checklistDone = checklistDone;
    }

    private void init(Task c) {
        if (c != null) {
            this.id = c.getId();
            this.name = c.getName();
            this.description = c.getDescription();
            this.priority = c.getPriority();
            this.assignee = c.getAssignee() != null ? new UserWrapper(c.getAssignee()) : new UserWrapper();
            this.cursoCase = c.getCursoCase() != null ? new CaseWrapper(c.getCursoCase()) : null;
            this.finished = c.isFinished();
            this.deadline = c.getDeadline();
            this.locality = c.getLocality() != null ? c.getLocality().getName() : null;
            this.listing = c.getListing() != null ? new ListingWrapper(c.getListing()) : null;
            this.plannedOn = c.getPlannedOn();
            this.finishedOn = c.getFinishedOn();
            this.finishedState = c.getFinishedState();
            this.priorityOnScreen = c.getPriorityOnScreen();
            this.problemText = c.getProblemText();
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

    @XmlElement
    public String getDescription() {
        return description;
    }

    @XmlElement
    public TaskPriority getPriority() {
        return priority;
    }

    @XmlElement
    public UserWrapper getAssignee() {
        return assignee;
    }

    @XmlElement
    public CaseWrapper getCursoCase() {
        return cursoCase;
    }

    @XmlElement
    public boolean getChecklistDone() {
        return checklistDone;
    }

    @XmlElement
    public boolean isFinished() {
        return finished;
    }

    @XmlElement
    public Date getDeadline() {
        return deadline;
    }

    @XmlElement
    public String getLocality() {
        return locality;
    }

    @XmlElement
    public ListingWrapper getListing() {
        return listing;
    }

    @XmlElement
    public Date getPlannedOn() {
        return plannedOn;
    }

    @XmlElement
    public Date getFinishedOn() {
        return finishedOn;
    }

    @XmlElement
    public TaskFinishState getFinishedState() {
        return finishedState;
    }

    @XmlElement
    public Long getPriorityOnScreen() {
        return priorityOnScreen;
    }

    public void setPriorityOnScreen(Long priorityOnScreen) {
        this.priorityOnScreen = priorityOnScreen;
    }
    
    @XmlElement
    public String getProblemText() {
        return problemText;
    }

    public void setProblemText(String problemText) {
        this.problemText = problemText;
    }

    public void setFinishedOn(Date finishedOn) {
        this.finishedOn = finishedOn;
    }

    public void setFinishedState(TaskFinishState finishedState) {
        this.finishedState = finishedState;
    }

    public void setPlannedOn(Date plannedOn) {
        this.plannedOn = plannedOn;
    }

    public void setListing(ListingWrapper listing) {
        this.listing = listing;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setCursoCase(CaseWrapper cursoCase) {
        this.cursoCase = cursoCase;

    }

    public void setChecklistDone(boolean checklistDone) {
        this.checklistDone = checklistDone;
    }

    public void setAssignee(UserWrapper assignee) {
        this.assignee = assignee;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
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

}
