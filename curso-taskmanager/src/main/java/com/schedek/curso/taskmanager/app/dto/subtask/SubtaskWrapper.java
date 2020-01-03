/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.subtask;

import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.enums.SubtaskState;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dan Nguyen
 */
@XmlRootElement
public class SubtaskWrapper {

    private Long id;
    private String name;
    private SubtaskState state;

    public SubtaskWrapper() {
    }

    public SubtaskWrapper(Subtask s) {
        if (s != null) {
            this.id = s.getId();
            this.name = s.getName();
            this.state = s.getState();
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

    @XmlElement
    public SubtaskState getState() {
        return state;
    }

    public void setState(SubtaskState state) {
        this.state = state;
    }

}
