package com.schedek.curso.ejb.entities;

import com.schedek.curso.ejb.embeddables.Coordinates;
import com.schedek.curso.ejb.enums.*;
import com.schedek.curso.ejb.util.UnObj;
import com.schedek.curso.ejb.view.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class Listing extends UnObj implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created = new Date();
    @Embedded
    private Coordinates position = new Coordinates();
    private Integer capacity;
    private String address;
    private boolean disabled = false;

    @OneToMany(mappedBy = "listing", fetch = FetchType.LAZY)
    private List<ListingBookingOverview> listingBookingOverview;

    @OneToOne(mappedBy = "listing", fetch = FetchType.LAZY)
    private ViewListingCurrentBooking currentBookingView;

    @ManyToOne
    private Locality locality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Coordinates getPosition() {
        if (position == null) {
            position = new Coordinates();
        }
        return position;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }



    public ViewListingCurrentBooking getCurrentBookingView() {
        return currentBookingView;
    }

    public void setCurrentBookingView(ViewListingCurrentBooking currentBookingView) {
        this.currentBookingView = currentBookingView;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ListingBookingOverview> getListingBookingOverview() {
        return listingBookingOverview;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }


    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
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
        if (!(object instanceof Listing)) {
            return false;
        }
        Listing other = (Listing) object;
        if (this.id == null && other.id == null) {
            return super.equals(object);
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.schedek.curso.ejb.entities.Listing[ id=" + id + " ]";

    }
}
