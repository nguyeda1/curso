/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.view;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.Locality;
import com.schedek.curso.ejb.entities.Task;
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
@Table(name = "v_task_stats")
@Cacheable(false)
public class ViewTaskStats implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private Task task;
    private Long prioritynumber;
    private Long unbegun;
    private Long fail;
    private Long done;
    private Long total;
    private String localityName;
    private String assigneeName;
    private String caseName;
    private String listingName;

    public String getListingName() {
        return listingName;
    }

    public void setListingName(String listingName) {
        this.listingName = listingName;
    }
    
    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
    
    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String locality) {
        this.localityName = locality;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getPrioritynumber() {
        return prioritynumber;
    }

    public void setPrioritynumber(Long prioritynumber) {
        this.prioritynumber = prioritynumber;
    }

    public Long getUnbegun() {
        return unbegun;
    }

    public void setUnbegun(Long unbegun) {
        this.unbegun = unbegun;
    }

    public Long getFail() {
        return fail;
    }

    public void setFail(Long fail) {
        this.fail = fail;
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

}
