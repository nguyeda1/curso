/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade.booking;

/**
 *
 * @author Razzier
 */
public enum QueryMode {
    START, START_ALERT, END, CLEAN;

    public String getDateColumn() {
        switch (this) {
            case START_ALERT:
            case START:
                return "startDate";
            case END:
                return "endDate";
            case CLEAN:
                return "cleanupTimestamp";
            default:
                throw new AssertionError();
        }
    }

    public String getTimeColumn() {
        switch (this) {
            case START:
            case START_ALERT:
                return "startTime";
            case END:
                return "endTime";
            case CLEAN:
                return "cleanupTimestamp";
            default:
                throw new AssertionError();
        }
    }
    
}
