/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.util;

import com.schedek.curso.ejb.entities.Listing;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author adakl
 */
public class CalendarData {
    

    public Listing listing;
    public Date from;
    public Date to;
    public BigDecimal price;
    public Boolean available;
    public Integer minimumStay;
    
    public static final int MINIMUM_STAY = 2;

    public static Date getDateOnly(Date fromd) {
        Calendar c = Calendar.getInstance();
        c.setTime(fromd);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getNextDate(Date d) {
        Date date = getDateOnly(d);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    public static Date getFirstDateOfCurrentMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getDateOnly(date));
        c.set(Calendar.DATE, 1);
        return c.getTime();
    }

    public static Date getLastDateOfCurrentMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getDateOnly(date));
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date getDateAdd(Date fromd, int toadd) {
        Calendar c = Calendar.getInstance();
        c.setTime(fromd);
        c.add(Calendar.DAY_OF_MONTH, toadd);
        return c.getTime();
    }
    
    public static Date getDateAddWorkingDays(Date fromd, int toadd) {
        Calendar c = Calendar.getInstance();
        c.setTime(fromd);
        for (int i = 0; i < toadd; i++)
            do {
                c.add(Calendar.DAY_OF_MONTH, 1);
            } while (!isWorkingDay(c));
        return c.getTime();
    }
    
    public static boolean isWorkingDay(Calendar cal) {
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return !(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
    }

    public static Date getFirstDateOfMonthInFuture(Date date, Integer move) {
        Calendar c = Calendar.getInstance();
        c.setTime(CalendarData.getDateOnly(date));
        c.add(Calendar.MONTH, move - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date getLastDateOfMonthInFuture(Date date, Integer move) {
        Calendar c = Calendar.getInstance();
        c.setTime(CalendarData.getDateOnly(date));
        c.add(Calendar.MONTH, move - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }
    
    public static int getDiffYears(Date first, Date last) {
        Calendar a = Calendar.getInstance();
        Calendar b = Calendar.getInstance();
        a.setTime(first);
        b.setTime(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

}
