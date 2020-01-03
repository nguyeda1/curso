/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto;

import com.schedek.curso.taskmanager.app.dto.user.UserWrapper;
import com.schedek.curso.ejb.entities.User;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dan Nguyen
 */
@XmlRootElement
public class JwtAuthWrapper {

    private String accessToken;
    private UserWrapper user;

    public JwtAuthWrapper() {
    }

    public JwtAuthWrapper(String accessToken, User u, String baseUrl) {
        this.accessToken = accessToken;
        this.user = u != null ? new UserWrapper(u, baseUrl) : null;
    }

    @XmlElement
    public String getAccessToken() {
        return accessToken;
    }

    @XmlElement
    public UserWrapper getUser() {
        return user;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setUser(UserWrapper user) {
        this.user = user;
    }
}
