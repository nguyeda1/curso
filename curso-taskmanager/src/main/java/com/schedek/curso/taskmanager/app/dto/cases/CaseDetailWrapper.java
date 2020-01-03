/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.cases;

import com.schedek.curso.ejb.entities.Activity;
import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.entities.Task;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tomáš
 */
@XmlRootElement
public class CaseDetailWrapper implements Serializable {

    private CaseWrapper cursoCase;
    private Long taskCount;
    private List<CaseTagWrapper> tags;

    public CaseDetailWrapper() {
    }

    public CaseDetailWrapper(Case c, Long taskCount) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.cursoCase = c != null ? new CaseWrapper(c) : null;
        this.taskCount = taskCount;
        if (c != null) {
            c.getTags().forEach(t -> {
                this.tags.add(new CaseTagWrapper(t));
            });
        }

    }

    @XmlElement
    public CaseWrapper getCursoCase() {
        return cursoCase;
    }

    public void setCursoCase(CaseWrapper cursoCase) {
        this.cursoCase = cursoCase;
    }

    @XmlElement
    public List<CaseTagWrapper> getTags() {
        return tags;
    }

    public void setTags(List<CaseTagWrapper> tags) {
        this.tags = tags;
    }

    @XmlElement
    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }

}
