/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.beans;

import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.taskmanager.app.security.JwtTokenProvider;
import java.io.Serializable;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Dan Nguyen
 */
@Named(value = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {

    @Inject
    private JwtTokenProvider jwtProvider;
    @EJB
    private UserFacade uf;
    private User user;

    public SessionBean() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void requireUser() {
        if (getUser() == null) {
            throw new WebApplicationException(401);
        }
    }

    public boolean isAuthenticated(String authToken) {
//        Long userId = null;
//        String filteredToken = jwtProvider.filterToken(authToken);
//        if (filteredToken != null && jwtProvider.validateToken(filteredToken)) {
//            userId = jwtProvider.getUserIdFromJWT(filteredToken);
//        } else {
//            throw new WebApplicationException(401);
//        }
        requireUser();
//        if (!Objects.equals(getUser().getId(), userId)) {
//            throw new WebApplicationException(401);
//        }
        return true;
    }

}
