/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseComment;
import com.schedek.curso.ejb.entities.CaseFeedback;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.TaskComment;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.CaseCommentFacade;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.TaskCommentFacade;
import com.schedek.curso.ejb.facade.TaskFacade;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.InfiniteList;
import com.schedek.curso.taskmanager.app.dto.feedback.CaseFeedbackWrapper;
import com.schedek.curso.taskmanager.app.dto.CommentWrapper;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Dan Nguyen
 */
@Path("/comment")
@ApplicationScoped
@Named
public class CommentController {

    @Inject
    SessionBean sb;
    @EJB
    TaskFacade tf;
    @EJB
    TaskCommentFacade tcf;
    @EJB
    CaseCommentFacade ccf;
    @EJB
    CaseFacade cf;
    @EJB
    ActivityFacade af;

    @Path("/task/{id}")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public InfiniteList<CommentWrapper> taskCommentList(@HeaderParam("Authorization") String authToken,
            @QueryParam("page") int pageNumber,
            @QueryParam("max") int pageSize,
            @PathParam("id") Long taskId) {
        sb.isAuthenticated(authToken);
        Task c = taskId != null ? tf.find(taskId) : null;
        List<CommentWrapper> wrappers = new ArrayList<>();
        tcf.findByTaskAndPage(c, pageNumber, pageSize).forEach(tc -> {
            wrappers.add(new CommentWrapper(tc));
        });
        return new InfiniteList(wrappers, tcf.getTaskCommentListCountByTask(c));
    }

    @Path("/task/{id}")
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json; charset=UTF-8"})
    public CommentWrapper createTaskComment(@HeaderParam("Authorization") String authToken,
            @PathParam("id") Long taskId,
            CommentWrapper object) {

        sb.isAuthenticated(authToken);
        User user = sb.getUser();
        Task c = taskId != null ? tf.find(taskId) : null;
        if (c == null) {
            throw new WebApplicationException(412);
        }
        TaskComment result = new TaskComment();

        result.setText(object.getText());

        result.setTask(c);
        result.setCreatedBy(user);
        result.setCreatedOn(new Date());
        tcf.create(result, sb.getUser());

        return new CommentWrapper(result);

    }

    @Path("/case/{id}")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public InfiniteList<CommentWrapper> caseCommentList(@HeaderParam("Authorization") String authToken,
            @QueryParam("page") int pageNumber,
            @QueryParam("max") int pageSize,
            @PathParam("id") Long caseId) {
        sb.isAuthenticated(authToken);
        Case c = caseId != null ? cf.find(caseId) : null;
        if (c == null) {
            throw new WebApplicationException(412);
        }
        List<CommentWrapper> wrappers = new ArrayList<>();
        ccf.findByCaseAndPage(c, pageNumber, pageSize).forEach(tc -> {
            wrappers.add(new CommentWrapper(tc));
        });
        return new InfiniteList(wrappers, Long.valueOf(ccf.getCaseCommentsCountByCase(c)));
    }

    @Path("/case/{id}")
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json; charset=UTF-8"})
    public CommentWrapper createCaseComment(@HeaderParam("Authorization") String authToken,
            @PathParam("id") Long caseId,
            CommentWrapper object) {

        sb.isAuthenticated(authToken);
        User user = sb.getUser();
        Case c = caseId != null ? cf.find(caseId) : null;
        if (c == null) {
            throw new WebApplicationException(412);
        }
        CaseComment result = new CaseComment();
        result.setText(object.getText());
        result.setCursoCase(c);
        result.setCreatedBy(user);
        result.setCreatedOn(new Date());
        ccf.create(result, user);
        return new CommentWrapper(result);
    }

}
