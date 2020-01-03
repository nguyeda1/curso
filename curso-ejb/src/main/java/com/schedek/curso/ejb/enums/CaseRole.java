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
public enum CaseRole {
    SALES("Sales"), COMMUNICATION("Communication"), OTHER("Other"), ALL("All"), MYREVIEWS("My reviews");

    private String label;

    private CaseRole() {
    }

    private CaseRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    public boolean isSales(){
        return equals(SALES);
    }
    
    public boolean isCommunication(){
        return equals(COMMUNICATION);
    }
        
    public boolean isOther(){
        return equals(OTHER);
    }
            
    public boolean isAll(){
        return equals(ALL);
    }
   
    public boolean isMyReviews(){
        return equals(MYREVIEWS);
    }

}
