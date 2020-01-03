/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.listing;

import com.schedek.curso.ejb.entities.Listing;
import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tomáš
 */
@XmlRootElement
public class ListingWrapper implements Serializable {

    private Long id;
    private String name;
    private String address;
    private String locality;

    public ListingWrapper() {
    }

    public ListingWrapper(Listing c) {
        if (c != null) {
            this.id = c.getId();
            this.name = c.getName();
            this.address = c.getAddress();
            this.locality = c.getLocality() == null ? "" : c.getLocality().getName();
        }
    }

    @XmlElement
    public Long getId() {
        return id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public String getAddress() {
        return address;
    }

    @XmlElement
    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final ListingWrapper other = (ListingWrapper) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
