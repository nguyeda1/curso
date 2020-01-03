/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.InfiniteList;
import com.schedek.curso.taskmanager.app.dto.listing.ListingWrapper;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Dan Nguyen
 */
@Path("/listing")
@ApplicationScoped
@Named
public class ListingController {

    @Inject
    SessionBean sb;
    @EJB
    ListingFacade lf;

    @Path("/init")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public InfiniteList<ListingWrapper> init(@HeaderParam("Authorization") String authToken,
            @QueryParam("page") int pageNumber,
            @QueryParam("max") int pageSize) {
        sb.isAuthenticated(authToken);
        List<ListingWrapper> wrappers = new ArrayList<>();
        lf.findByPage(pageNumber, pageSize).forEach((l) -> {
            wrappers.add(new ListingWrapper(l));
        });
        return new InfiniteList(wrappers, lf.getListingCount());
    }
    
    @Path("/search")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public List<ListingWrapper> search(@HeaderParam("Authorization") String authToken,
            @QueryParam("query") String query) {
        sb.isAuthenticated(authToken);
        List<ListingWrapper> wrappers = new ArrayList<>();
        lf.asyncSearch(query).forEach((l) -> {
            wrappers.add(new ListingWrapper(l));
        });
        return wrappers;
    }

    @GET
    @Produces({"application/json; charset=UTF-8"})
    public ListingWrapper searchById(@HeaderParam("Authorization") String authToken,
            @QueryParam("id") Long id) {
        sb.isAuthenticated(authToken);
        return new ListingWrapper(lf.find(id));
    }

}
