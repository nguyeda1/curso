/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade.qb;

import com.schedek.curso.ejb.facade.booking.BookingDay;
import java.util.Comparator;

/**
 *
 * @author Razzier
 */
public class BookingDayComparator implements Comparator<BookingDay> {

    public int compare(BookingDay o1, BookingDay o2) {
        if (o1 == null || o1.getDate() == null) {
            return -1;
        }
        if (o2 == null || o2.getDate() == null) {
            return 1;
        }
        return o1.getDate().compareTo(o2.getDate());
    }

}
