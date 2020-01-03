package com.schedek.curso.web.beans.app.booking;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.web.beans.list.AbstractListState;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Named;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import org.apache.commons.lang3.time.DateUtils;

@Named(value = "bookingListState")
@ViewScoped
public class BookingListState extends AbstractListState {

    private List<Booking> filteredValue;

    public BookingListState() {
    }

    public List<Booking> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Booking> filteredValue) {
        this.filteredValue = filteredValue;
    }

    @Override
    public void setFilteredValueInt(List filteredValue) {
        this.filteredValue = filteredValue;
    }

    public boolean filterByDate(Object value, Object filter, Locale locale){
        if (filter == null) {
            return true;
        }
        if (value == null) {
            return false;
        }
        return DateUtils.truncatedEquals((Date) filter, (Date) value, Calendar.DATE);
    }
}
