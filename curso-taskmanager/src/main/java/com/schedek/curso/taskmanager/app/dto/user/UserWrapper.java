/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.user;

import com.schedek.curso.ejb.entities.User;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tomáš
 */
@XmlRootElement
public class UserWrapper implements Serializable {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String phone;
    private Date lastActivityFetch;
    private Date lastActivityRead;
    private String baseUrl;

    public UserWrapper() {
    }

    public UserWrapper(User c, String baseUrl) {
        if (c != null) {
            this.id = c.getId();
            this.name = c.getFirstname();
            this.surname = c.getLastname();
            this.username = c.getUsername();
            this.phone = c.getPhone();
            this.lastActivityFetch = c.getLastActivityFetch();
            this.lastActivityRead = c.getLastActivityRead();
        }
        this.baseUrl = baseUrl;
    }
    
    public UserWrapper(User c) {
        if (c != null) {
            this.id = c.getId();
            this.name = c.getFirstname();
            this.surname = c.getLastname();
            this.username = c.getUsername();
            this.phone = c.getPhone();
            this.lastActivityFetch = c.getLastActivityFetch();
            this.lastActivityRead = c.getLastActivityRead();
        }
    }

    @XmlElement
    public Long getId() {
        return id;
    }

    @XmlElement
    public String getUsername() {
        return username;
    }

    @XmlElement
    public String getLastname() {
        return surname;
    }

    @XmlElement
    public String getFirstname() {
        return name;
    }

    @XmlElement
    public String getFullname() {
        return surname + " " + name;
    }

    @XmlElement
    public String getPhone() {
        return phone;
    }

    @XmlElement
    public Date getLastActivityFetch() {
        return lastActivityFetch;
    }

    @XmlElement
    public Date getLastActivityRead() {
        return lastActivityRead;
    }

    @XmlElement
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setLastActivityFetch(Date lastActivityFetch) {
        this.lastActivityFetch = lastActivityFetch;
    }

    public void setLastActivityRead(Date lastActivityRead) {
        this.lastActivityRead = lastActivityRead;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
