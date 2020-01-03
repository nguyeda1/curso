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
public enum CaseFeedbackType {
    EMAIL, PHONE, OTHER, NONE;

    public String getString() {
        switch (this) {
            case EMAIL:
                return "Email";
            case PHONE:
                return "Phone";
            case OTHER:
                return "Other";
            case NONE:
                return "None";
        }
        return null;
    }

    public boolean isNone() {
        return this.equals(NONE);
    }
}
