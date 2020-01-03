/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Activity;
import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseFeedback;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.BookingFacade;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.CaseFeedbackFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.ejb.facade.TaskFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.InfiniteList;
import com.schedek.curso.taskmanager.app.dto.feedback.CaseFeedbackWrapper;
import com.schedek.curso.taskmanager.app.dto.cases.CaseWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Dan Nguyen
 */
@Path("/feedback")
@ApplicationScoped
@Named
public class CaseFeedbackController {

    @Inject
    SessionBean sb;
    @EJB
    CaseFacade cf;
    @EJB
    CaseFeedbackFacade crf;
    @EJB
    TaskFacade tf;
    @EJB
    ActivityFacade af;
    @EJB
    UserFacade uf;
    @EJB
    ListingFacade lf;
    @EJB
    BookingFacade bf;

    @Path("/init/{id}")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public InfiniteList<CaseFeedbackWrapper> getFeedbackListByCase(@HeaderParam("Authorization") String authToken,
            @QueryParam("page") int pageNumber,
            @QueryParam("max") int pageSize,
            @PathParam("id") Long caseId) {
        sb.isAuthenticated(authToken);
        Case c = caseId != null ? cf.find(caseId) : null;

        List<CaseFeedbackWrapper> wrappers = new ArrayList<>();
        crf.findByCaseAndPage(c, pageNumber, pageSize).forEach(f -> {
            wrappers.add(new CaseFeedbackWrapper(f));
        });

        return new InfiniteList(wrappers, crf.getFeedbackListCountByCase(c));
    }

    @Path("/{id}")
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json; charset=UTF-8"})
    public CaseFeedbackWrapper createFeedback(@HeaderParam("Authorization") String authToken,
            @PathParam("id") Long caseId,
            CaseFeedbackWrapper object) {

        sb.isAuthenticated(authToken);
        User user = sb.getUser();
        Case c = caseId != null ? cf.find(caseId) : null;
        if (c == null || c.getCaseState() != CaseState.REVIEW) {
            throw new WebApplicationException(412);
        }
        CaseFeedback result = new CaseFeedback();

        result.setDescription(object.getDescription());
        if (object.getGuestInformed() != null) {
            result.setGuestInformed(object.getGuestInformed());
        } else if (object.getHostInformed() != null) {
            result.setHostInformed(object.getHostInformed());
        }
        result.setType(object.getType());

        result.setCursoCase(c);
        result.setReviewedBy(user);
        result.setReviewedOn(new Date());
        crf.create(result, user);

        return new CaseFeedbackWrapper(result);

    }

}
