/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.feedback;

import com.schedek.curso.ejb.entities.CaseFeedback;
import com.schedek.curso.ejb.enums.CaseFeedbackType;
import com.schedek.curso.taskmanager.app.dto.user.UserWrapper;
import com.schedek.curso.taskmanager.app.dto.cases.CaseWrapper;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dan Nguyen
 */
@XmlRootElement
public class CaseFeedbackWrapper {

    private Long id;
    private CaseWrapper cursoCase;
    private Boolean hostInformed;
    private Boolean guestInformed;
    private String description;
    private CaseFeedbackType type;
    private UserWrapper reviewedBy;
    private Date reviewedOn;

    public CaseFeedbackWrapper() {
    }
    
    public CaseFeedbackWrapper(CaseFeedback c) {
        this.id = c.getId();
        this.cursoCase = c.getCursoCase() != null ? new CaseWrapper(c.getCursoCase()) : null;
        this.hostInformed = c.isHostInformed();
        this.guestInformed = c.isGuestInformed();
        this.description = c.getDescription();
        this.type = c.getType();
        this.reviewedBy = c.getReviewedBy() != null ? new UserWrapper(c.getReviewedBy()) : null;
        this.reviewedOn = c.getReviewedOn();
    }

    @XmlElement
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement
    public CaseWrapper getCursoCase() {
        return cursoCase;
    }

    public void setCursoCase(CaseWrapper cursoCase) {
        this.cursoCase = cursoCase;
    }

    @XmlElement
    public Boolean getHostInformed() {
        return hostInformed;
    }

    public void setHostInformed(Boolean hostInformed) {
        this.hostInformed = hostInformed;
    }

    @XmlElement
    public Boolean getGuestInformed() {
        return guestInformed;
    }

    public void setGuestInformed(Boolean guestInformed) {
        this.guestInformed = guestInformed;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement
    public CaseFeedbackType getType() {
        return type;
    }

    public void setType(CaseFeedbackType type) {
        this.type = type;
    }

    @XmlElement
    public UserWrapper getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(UserWrapper reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    @XmlElement
    public Date getReviewedOn() {
        return reviewedOn;
    }

    public void setReviewedOn(Date reviewedOn) {
        this.reviewedOn = reviewedOn;
    }

}
