/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.activity;

import com.schedek.curso.ejb.entities.Activity;
import com.schedek.curso.ejb.entities.CaseActivity;
import com.schedek.curso.ejb.entities.TaskActivity;
import com.schedek.curso.ejb.enums.ActivityChange;
import com.schedek.curso.ejb.enums.ActivityType;
import com.schedek.curso.ejb.xmladapter.DateCzAdapter;
import com.schedek.curso.taskmanager.app.dto.cases.CaseWrapper;
import com.schedek.curso.taskmanager.app.dto.task.TaskWrapper;
import com.schedek.curso.taskmanager.app.dto.user.UserWrapper;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Tomáš
 */
@XmlRootElement
public class ActivityWrapper implements Serializable {

    private Long id;
    private Date createdOn;
    private UserWrapper createdBy;
    private String log;
    private TaskWrapper task;
    private CaseWrapper cursoCase;
    private ActivityType type;
    private Set<ActivityChange> changes;

    public ActivityWrapper() {
    }

    public ActivityWrapper(Activity c) {
        init(c);
    }

    public ActivityWrapper(TaskActivity c) {
        init(c);
        this.task = c.getTask() != null ? new TaskWrapper(c.getTask()) : null;
    }

    public ActivityWrapper(CaseActivity c) {
        init(c);
        this.cursoCase = c.getCursoCase() != null ? new CaseWrapper(c.getCursoCase()) : null;
    }

    private void init(Activity c) {
        this.id = c.getId();
        this.createdOn = c.getCreatedOn();
        this.createdBy = c.getCreatedBy() != null ? new UserWrapper(c.getCreatedBy()) : null;
        this.log = c.getLog();
        this.type = c.getType();
        this.changes = c.getChanges();
    }

    @XmlElement
    public Long getId() {
        return id;
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
    public String getLog() {
        return log;
    }

    @XmlElement
    public TaskWrapper getTask() {
        return task;
    }

    public void setTask(TaskWrapper task) {
        this.task = task;
    }

    @XmlElement
    public CaseWrapper getCursoCase() {
        return cursoCase;
    }

    public void setCursoCase(CaseWrapper cursoCase) {
        this.cursoCase = cursoCase;
    }

    @XmlElement
    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    @XmlElement
    public Set<ActivityChange> getChanges() {
        return changes;
    }

    public void setChanges(Set<ActivityChange> changes) {
        this.changes = changes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedBy(UserWrapper createdBy) {
        this.createdBy = createdBy;
    }

    public void setLog(String log) {
        this.log = log;
    }

}
