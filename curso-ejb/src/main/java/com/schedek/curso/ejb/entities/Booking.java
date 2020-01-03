package com.schedek.curso.ejb.entities;

import com.schedek.curso.ejb.embeddables.Price;
import com.schedek.curso.ejb.enums.*;

import static com.schedek.curso.ejb.enums.BookingType.AIRBNB;
import static com.schedek.curso.ejb.enums.BookingType.BOOKING;
import static com.schedek.curso.ejb.enums.BookingType.OTHER;

import com.schedek.curso.ejb.util.BigDec;
import com.schedek.curso.ejb.util.CalendarData;
import com.schedek.curso.ejb.view.ViewBookingStats;
import com.schedek.curso.ejb.xmladapter.BigDecimalAdapter;
import com.schedek.curso.ejb.xmladapter.DateCzAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY)
    private ViewBookingStats stats;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created = new Date();
    @ManyToOne
    private Listing listing;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date endTime;
    private String guestName;
    @Column(nullable = false)
    private Long guestCount;
    @Enumerated(EnumType.STRING)
    @Column(name = "bookingstate")
    private BookingState state = BookingState.NEW;
    @ManyToOne
    public User createdBy;

    private boolean canceled = false;
    @ManyToOne
    private User canceledBy;

    @Embedded
    private Price price = new Price();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @XmlTransient
    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @XmlTransient
    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    @XmlJavaTypeAdapter(DateCzAdapter.class)
    public Date getStartDate() {
        return startDate;
    }

    public String getStartDateStr() {
        SimpleDateFormat sd = new SimpleDateFormat("d.M.yyyy");
        return sd.format(getStartDate());
    }

    public void setStartDate(Date startDate) {

        this.startDate = startDate;
    }

    @XmlJavaTypeAdapter(DateCzAdapter.class)
    public Date getEndDate() {

        return endDate;
    }

    public String getEndDateStr() {
        SimpleDateFormat sd = new SimpleDateFormat("d.M.yyyy");
        return sd.format(getEndDate());
    }

    public void setEndDate(Date endDate) {

        this.endDate = endDate;
    }

    public Date getStartTime() {

        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public BookingState getState() {
        return state;
    }

    public void setState(BookingState state) {
        this.state = state;
    }

    public Long getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Long guestCount) {
        this.guestCount = guestCount;
    }

    public Date getStartTimestamp() {
        return mergeDT(startDate, startTime);
    }

    public Date getEndTimestamp() {
        return mergeDT(endDate, endTime);
    }

    private Date mergeDT(Date date, Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (time != null) {
            Calendar d = Calendar.getInstance();
            d.setTime(time);
            c.set(Calendar.HOUR_OF_DAY, d.get(Calendar.HOUR_OF_DAY));
            c.set(Calendar.MINUTE, d.get(Calendar.MINUTE));
            c.set(Calendar.SECOND, d.get(Calendar.SECOND));
            c.set(Calendar.MILLISECOND, d.get(Calendar.MILLISECOND));
        }
        return c.getTime();
    }

    public Price getPrice() {
        if (price == null) {
            price = new Price();
        }
        return price;
    }


    public void setPrice(Price price) {

        this.price = price;

    }

    public ViewBookingStats getStats() {
        return stats;
    }

    public void setStats(ViewBookingStats stats) {
        this.stats = stats;
    }

    public int getDaysCount() {
        return Math.round((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public long getGuestDaysCount() {
        return getDaysCount() * getGuestCount();

    }

    public void setDaysCount(int v) {

    }
    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public User getCanceledBy() {
        return canceledBy;
    }

    public void setCanceledBy(User canceledBy) {
        this.canceledBy = canceledBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.schedek.curso.ejb.entities.Booking[ id=" + id + " ]";
    }

    public void mergeBookings(Booking received) {
        this.setStartDate(received.getStartDate());
        this.setEndDate(received.getEndDate());
        this.setEndTime(received.getEndTime());
        this.setListing(received.getListing());
        this.setGuestCount(received.getGuestCount());
        this.setGuestName(received.getGuestName());
        this.setPrice(received.getPrice());
    }

}
