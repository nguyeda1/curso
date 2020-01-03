package com.schedek.curso.ejb.view;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.entities.Listing;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "v_listing_current_booking")
@Cacheable(false)
public class ViewListingCurrentBooking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    @OneToOne
    private Listing listing;
    @OneToOne
    private Booking currentBooking;
    @OneToOne
    private Booking lastBooking;

    public ViewListingCurrentBooking() {
    }   
    
    public Long getId() {
        return id;
    }
    
    public Listing getListing() {
        return listing;
    }

    public Booking getCurrentBooking() {
        return currentBooking;
    }

    public Booking getLastBooking() {
        return lastBooking;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (listing != null ? listing.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ViewListingCurrentBooking)) {
            return false;
        }
        ViewListingCurrentBooking other = (ViewListingCurrentBooking) object;
        if ((this.listing == null && other.listing != null) || (this.listing != null && !this.listing.equals(other.listing))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.schedek.curso.ejb.view.NewEntity[ id=" + listing.getId() + " ]";
    }
    
}
