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
public enum CaseState {
    NEW("New"), IN_PROGRESS("In progress"), REVIEW("Review"), DONE("Done");

    private String label;

    private CaseState() {
    }

    private CaseState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    public boolean isNew(){
        return equals(NEW);
    }
	
	public boolean isInProgress(){
		return equals(IN_PROGRESS);
	}
	
	public boolean isReview(){
		return equals(REVIEW);
	}
	public boolean isDone(){
		return equals(DONE);
	}

}
