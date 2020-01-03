package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.CaseTagFacade;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.cases.CaseTagWrapper;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Dan Nguyen
 */
@Path("/tag")
@ApplicationScoped
@Named
public class CaseTagController {

    @Inject
    SessionBean sb;
    @EJB
    CaseTagFacade ctf;
    @EJB
    CaseFacade cf;
    @EJB
    ActivityFacade af;

    @Path("/init")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public List<CaseTagWrapper> init(@HeaderParam("Authorization") String authToken) {
        sb.isAuthenticated(authToken);
        List<CaseTagWrapper> res = new ArrayList<>();
        ctf.findAll().forEach(t -> {
            res.add(new CaseTagWrapper(t));
        });
        return res;
    }

    @Path("/add/{id}")
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json; charset=UTF-8"})
    public List<CaseTagWrapper> addToCase(@HeaderParam("Authorization") String authToken,
            @PathParam("id") Long caseId, List<CaseTagWrapper> tagWrappers) {
        sb.isAuthenticated(authToken);
        Case c = cf.find(caseId);
        List<CaseTag> newtags = new ArrayList();
        tagWrappers.forEach(wrapper -> {
            CaseTag tag = ctf.find(wrapper.getId());
            newtags.add(tag);
        });
        c.setTags(newtags);
        cf.edit(c, sb.getUser());
        return tagWrappers;
    }

}
