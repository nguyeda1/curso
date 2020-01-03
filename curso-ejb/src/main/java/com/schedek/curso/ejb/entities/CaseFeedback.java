/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.entities;

import com.schedek.curso.ejb.enums.CaseFeedbackType;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dan Nguyen
 */
@Entity
@XmlRootElement
public class CaseFeedback implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Case cursoCase;
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean hostInformed = false;
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean guestInformed = false;
    private String description;
    @Enumerated(EnumType.STRING)
    private CaseFeedbackType type;
    @ManyToOne
    private User reviewedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewedOn;

    public CaseFeedback() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Case getCursoCase() {
        return cursoCase;
    }

    public void setCursoCase(Case cursoCase) {
        this.cursoCase = cursoCase;
    }

    public boolean isHostInformed() {
        return hostInformed;
    }

    public void setHostInformed(boolean hostInformed) {
        this.hostInformed = hostInformed;
    }

    public boolean isGuestInformed() {
        return guestInformed;
    }

    public void setGuestInformed(boolean guestInformed) {
        this.guestInformed = guestInformed;
    }

    public CaseFeedbackType getType() {
        return type;
    }

    public void setType(CaseFeedbackType type) {
        this.type = type;
    }

    public User getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(User reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Date getReviewedOn() {
        return reviewedOn;
    }

    public void setReviewedOn(Date reviewedOn) {
        this.reviewedOn = reviewedOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CaseFeedback)) {
            return false;
        }
        CaseFeedback other = (CaseFeedback) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.schedek.curso.ejb.entities.CaseReview[ id=" + id + " ]";
    }

}
