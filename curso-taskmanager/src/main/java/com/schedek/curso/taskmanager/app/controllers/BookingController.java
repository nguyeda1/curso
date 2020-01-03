/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.facade.BookingFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.InfiniteList;
import com.schedek.curso.taskmanager.app.dto.booking.BookingWrapper;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Dan Nguyen
 */
@Path("/booking")
@ApplicationScoped
@Named
public class BookingController {

    @Inject
    SessionBean sb;
    @EJB
    BookingFacade bf;
    @EJB
    ListingFacade lf;
//    
//    @Path("/init")
//    @GET
//    @Produces({"application/json; charset=UTF-8"})
//    public List<BookingWrapper> init() {
//         sb.isAuthenticated(authToken);
//        List<BookingWrapper> bookings = new ArrayList<>();
//        bf.findAll().forEach((b) -> {
//            bookings.add(new BookingWrapper(b));
//        });
//        return bookings;
//    }

    @Path("/by-listing/{id}")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public InfiniteList<BookingWrapper> findByListing(@HeaderParam("Authorization") String authToken,
            @PathParam("id") Long listingId,
            @QueryParam("page") int pageNumber,
            @QueryParam("max") int pageSize) {
        sb.isAuthenticated(authToken);
        Listing l = lf.find(listingId);

        List<BookingWrapper> wrappers = new ArrayList<>();
        bf.findByListingAndPage(l, pageNumber, pageSize).forEach((b) -> {
            wrappers.add(new BookingWrapper(b));
        });
        return new InfiniteList(wrappers, bf.getBookingCountByListing(l));
    }

    @Path("/search")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public List<BookingWrapper> search(@HeaderParam("Authorization") String authToken,
            @QueryParam("listingId") Long listingId,
            @QueryParam("query") String query) {
        sb.isAuthenticated(authToken);
        Listing l = listingId != null ? lf.find(listingId) : null;
        List<BookingWrapper> wrappers = new ArrayList<>();
        bf.asyncSearch(l, query).forEach((b) -> {
            wrappers.add(new BookingWrapper(b));
        });
        return wrappers;
    }
    
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public BookingWrapper searchById(@HeaderParam("Authorization") String authToken,
            @QueryParam("listingId") Long listingId,
            @QueryParam("id") Long id) {
        sb.isAuthenticated(authToken);
        Listing l = listingId != null ? lf.find(listingId) : null;
        return new BookingWrapper(bf.findByIdAndListing(l, id));
    }

}
