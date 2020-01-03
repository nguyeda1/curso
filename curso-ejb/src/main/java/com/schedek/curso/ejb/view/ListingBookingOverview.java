package com.schedek.curso.ejb.view;

import com.schedek.curso.ejb.entities.Listing;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "v_listing_booking_overview")
@Cacheable(false)
public class ListingBookingOverview implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Listing listing;
    @Id
    @Temporal(TemporalType.DATE)
    private Date month;
    @Id
    private String currency_code;
    private BigDecimal sum;
    @Column(name = "gross_sum")
    private BigDecimal grossSum;
    private BigDecimal avg;
    @Column(name = "gross_avg")
    private BigDecimal grossAvg;
    private Integer daysOccupied;
    private BigDecimal rentprice;
    private Integer ownerBookings;

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getGrossSum() {
        return grossSum;
    }

    public void setGrossSum(BigDecimal grossSum) {
        this.grossSum = grossSum;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public BigDecimal getGrossAvg() {
        return grossAvg;
    }

    public void setGrossAvg(BigDecimal grossAvg) {
        this.grossAvg = grossAvg;
    }

    public Integer getDaysOccupied() {
        return daysOccupied;
    }

    public void setDaysOccupied(Integer daysOccupied) {
        this.daysOccupied = daysOccupied;
    }

    public BigDecimal getRentprice() {
        return rentprice;
    }

    public Integer getOwnerBookings() {
        return ownerBookings;
    }

    public void setOwnerBookings(Integer ownerBookings) {
        this.ownerBookings = ownerBookings;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.listing);
        hash = 59 * hash + Objects.hashCode(this.month);
        hash = 59 * hash + Objects.hashCode(this.currency_code);
        hash = 59 * hash + Objects.hashCode(this.sum);
        hash = 59 * hash + Objects.hashCode(this.avg);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ListingBookingOverview other = (ListingBookingOverview) obj;
        if (!Objects.equals(this.currency_code, other.currency_code)) {
            return false;
        }
        if (!Objects.equals(this.listing, other.listing)) {
            return false;
        }
        if (!Objects.equals(this.month, other.month)) {
            return false;
        }
        if (!Objects.equals(this.sum, other.sum)) {
            return false;
        }
        if (!Objects.equals(this.avg, other.avg)) {
            return false;
        }
        return true;
    }

}
