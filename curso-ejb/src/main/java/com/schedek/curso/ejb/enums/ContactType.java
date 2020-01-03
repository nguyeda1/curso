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
public enum ContactType {

    OPERATION, IT, COMMUNICATION, RECEPTION, RESERVATIONS_MANAGER, CLEANING_EMERGENCY, COMMUNICATION_MANAGER, INTERNAL_SALES;

    public boolean isOperation() {
        return equals(OPERATION);
    }

    public boolean isReception() {
        return equals(RECEPTION);
    }

    public boolean isCommunication() {
        return equals(COMMUNICATION);
    }

    public boolean isIt() {
        return equals(IT);
    }

    public boolean isReservationsManager() {
        return equals(RESERVATIONS_MANAGER);
    }

    public boolean isCleaningEmergency() {
        return equals(CLEANING_EMERGENCY);
    }

    public boolean isCommunicationManager() {
        return equals(COMMUNICATION_MANAGER);
    }

    public boolean isInternalSales() {
        return equals(INTERNAL_SALES);
    }

}
