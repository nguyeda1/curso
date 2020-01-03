/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.view;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.Locality;
import com.schedek.curso.ejb.entities.User;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author prace
 */
@Entity
@Table(name = "v_case_stats")
@Cacheable(false)
public class ViewCaseStats implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private Case casex;
    private Long finished;
    private Long inProgress;
    private Long done;
    private Long total;
    private Long prioritynumber;
    private String localityName;

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String locality) {
        this.localityName = locality;
    }
    
  
    public Case getCasex() {
        return casex;
    }

    public void setCasex(Case casex) {
        this.casex = casex;
    }

    public Long getFinished() {
        return finished;
    }

    public void setFinished(Long finished) {
        this.finished = finished;
    }

    public Long getInProgress() {
        return inProgress;
    }

    public void setInProgress(Long inProgress) {
        this.inProgress = inProgress;
    }

    public Long getDone() {
        return done;
    }

    public void setDone(Long done) {
        this.done = done;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPrioritynumber() {
        return prioritynumber;
    }

    public void setPrioritynumber(Long prioritynumber) {
        this.prioritynumber = prioritynumber;
    }

}
