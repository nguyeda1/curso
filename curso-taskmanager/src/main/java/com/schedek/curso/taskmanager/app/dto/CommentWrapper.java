/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto;

import com.schedek.curso.ejb.entities.CaseComment;
import com.schedek.curso.ejb.entities.TaskComment;
import com.schedek.curso.taskmanager.app.dto.user.UserWrapper;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dan Nguyen
 */
@XmlRootElement
public class CommentWrapper {

    private Long id;
    private Date createdOn;
    private UserWrapper assignee;
    private String text;

    public CommentWrapper() {
    }

    public CommentWrapper(TaskComment tc) {
        if (tc != null) {
            this.id = tc.getId();
            this.createdOn = tc.getCreatedOn();
            this.assignee = tc.getCreatedBy() != null ? new UserWrapper(tc.getCreatedBy()) : null;
            this.text = tc.getText();
        }
    }
    
    public CommentWrapper(CaseComment tc) {
        if (tc != null) {
            this.id = tc.getId();
            this.createdOn = tc.getCreatedOn();
            this.assignee = tc.getCreatedBy() != null ? new UserWrapper(tc.getCreatedBy()) : null;
            this.text = tc.getText();
        }
    }

    @XmlElement
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @XmlElement
    public UserWrapper getAssignee() {
        return assignee;
    }

    public void setAssignee(UserWrapper assignee) {
        this.assignee = assignee;
    }

    @XmlElement
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
