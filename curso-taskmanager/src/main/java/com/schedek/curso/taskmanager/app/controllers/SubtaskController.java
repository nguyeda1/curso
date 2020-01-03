package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.enums.TaskPriority;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.SubtaskFacade;
import com.schedek.curso.ejb.facade.TaskFacade;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.subtask.SubtaskWrapper;
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
import javax.ws.rs.WebApplicationException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Dan Nguyen
 */
@Path("/subtask")
@ApplicationScoped
@Named
public class SubtaskController {

    @Inject
    SessionBean sb;
    @EJB
    TaskFacade tf;
    @EJB
    SubtaskFacade stf;
    @EJB
    ActivityFacade af;

    @Path("/init/{id}")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public List<SubtaskWrapper> findByTask(@HeaderParam("Authorization") String authToken,
            @PathParam("id") Long taskId) {
        sb.isAuthenticated(authToken);
        Task t = taskId != null ? tf.find(taskId) : null;
        List<SubtaskWrapper> res = new ArrayList<>();
        stf.findByTask(t).forEach(s -> {
            res.add(new SubtaskWrapper(s));
        });
        return res;
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json; charset=UTF-8"})
    public SubtaskWrapper stateUpdate(@HeaderParam("Authorization") String authToken,
            SubtaskWrapper object) {
        sb.isAuthenticated(authToken);
        Subtask t = stf.find(object.getId());
        t.setState(object.getState());
        stf.edit(t,sb.getUser());
        return new SubtaskWrapper(t);
    }

}
