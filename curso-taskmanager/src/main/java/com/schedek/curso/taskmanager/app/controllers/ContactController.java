/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Contact;
import com.schedek.curso.ejb.facade.ContactFacade;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.contact.ContactWrapper;
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

/**
 *
 * @author Dan Nguyen
 */
@Path("/contact")
@ApplicationScoped
@Named
public class ContactController {

    @Inject
    SessionBean sb;
    @EJB
    ContactFacade cf;

    @Path("/init")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public List<ContactWrapper> init(@HeaderParam("Authorization") String authToken) {
        sb.isAuthenticated(authToken);
        List<ContactWrapper> contacts = new ArrayList<>();
        cf.findAll().forEach((c) -> {
            contacts.add(new ContactWrapper(c));
        });
        return contacts;
    }

}
