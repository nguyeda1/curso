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
public class MultipleReservationsException extends Exception {
    
    public MultipleReservationsException(String message) {
        super(message);
    }
    
}
