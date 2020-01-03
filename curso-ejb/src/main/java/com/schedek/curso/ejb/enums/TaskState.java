/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Dan Nguyen
 */
public enum TaskState {
    IN_PROGRESS("In progress"), WITH_PROBLEM("With problem"), DONE("Done");

    private String label;

    private TaskState() {
    }

    private TaskState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    public boolean isIn_Progress() {
		return equals(IN_PROGRESS);
    }
    
    public boolean isWith_problem() {
        return equals(WITH_PROBLEM);
    }

}
