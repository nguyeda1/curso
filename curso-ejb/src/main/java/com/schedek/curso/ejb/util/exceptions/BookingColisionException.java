/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.util.exceptions;

import javax.ejb.ApplicationException;
import javax.ejb.EJBException;

/**
 *
 * @author Adam
 */
@ApplicationException
public class BookingColisionException extends EJBException {

    public BookingColisionException(String message) {
        super(message);
    }

}