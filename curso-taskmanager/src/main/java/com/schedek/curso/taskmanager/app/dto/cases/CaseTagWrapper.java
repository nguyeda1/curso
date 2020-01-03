/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.cases;

import com.schedek.curso.ejb.entities.CaseTag;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dan Nguyen
 */
@XmlRootElement
public class CaseTagWrapper implements Serializable {

    private Long id;
    private String name;
    private List<Long> caseIds;

    public CaseTagWrapper() {
    }

    public CaseTagWrapper(CaseTag c) {
        if (c != null) {
            this.id = c.getId();
            this.name = c.getName();
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

}
