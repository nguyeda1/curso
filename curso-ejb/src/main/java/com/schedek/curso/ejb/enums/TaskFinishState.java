/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.enums;

/**
 *
 * @author Dan Nguyen
 */
public enum TaskFinishState {
    
    DONE("Done"), PARTIALLY("Partially Done"), NOT_DONE("Not Done"), NO_TIME("No Time");
    
    private final String label;
    
    private TaskFinishState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    public boolean isDone(){
        return equals(DONE);
    }
    
    public boolean isPartially(){
        return equals(PARTIALLY);
    }
    
    public boolean isNot_done(){
        return equals(NOT_DONE);
    }

    public boolean isNoTine(){
        return equals(NO_TIME);
    }
}
