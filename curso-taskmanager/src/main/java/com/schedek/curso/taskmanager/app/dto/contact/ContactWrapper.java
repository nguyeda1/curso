/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.contact;

import com.schedek.curso.ejb.entities.Contact;
import com.schedek.curso.ejb.enums.ContactType;
import com.schedek.curso.taskmanager.app.dto.user.UserWrapper;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tomáš
 */
@XmlRootElement
public class ContactWrapper implements Serializable {

    private Long id;
    private UserWrapper user;
    private ContactType type;

    public ContactWrapper() {
    }

    public ContactWrapper(Contact c) {
        if (c != null) {
            this.id = c.getId();
            this.user = c.getUser() != null ? new UserWrapper(c.getUser()) : null;
            this.type = c.getType();
        }
    }

    @XmlElement
    public Long getId() {
        return id;
    }

    @XmlElement
    public UserWrapper getUser() {
        return user;
    }

    @XmlElement
    public ContactType getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserWrapper user) {
        this.user = user;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

}
