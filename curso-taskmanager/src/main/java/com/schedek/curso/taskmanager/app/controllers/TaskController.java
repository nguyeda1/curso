/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.TaskFinishState;
import com.schedek.curso.ejb.enums.TaskPriority;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.SubtaskFacade;
import com.schedek.curso.ejb.facade.TaskCommentFacade;
import com.schedek.curso.ejb.facade.TaskFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.ejb.files.Filestore;
import com.schedek.curso.ejb.files.UploadType;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.InfiniteList;
import com.schedek.curso.taskmanager.app.dto.PhotoWrapper;
import com.schedek.curso.taskmanager.app.dto.activity.ActivityWrapper;
import com.schedek.curso.taskmanager.app.dto.task.TaskDetailWrapper;
import com.schedek.curso.taskmanager.app.dto.task.TaskWrapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author Dan Nguyen
 */
@Path("/task")
@ApplicationScoped
@Named
public class TaskController {

    @Inject
    SessionBean sb;
    @EJB
    TaskFacade tf;
    @EJB
    CaseFacade cf;
    @EJB
    UserFacade uf;
    @EJB
    TaskCommentFacade tcf;
    @EJB
    SubtaskFacade stf;
    @EJB
    ActivityFacade af;
    @EJB
    Filestore fs;

    @Path("/init/{id}")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public List<TaskWrapper> findByCase(@HeaderParam("Authorization") String authToken,
            @PathParam("id") Long caseId) {
        sb.isAuthenticated(authToken);
        Case c = caseId != null ? cf.find(caseId) : null;
        List<TaskWrapper> wrappers = new ArrayList();
        tf.findNotFinishedByCase(c).forEach((ta) -> {
            wrappers.add(new TaskWrapper(ta, stf.isAllBegunByTask(ta)));
        });
        return wrappers;
    }

    @Path("/init")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public InfiniteList<TaskWrapper> getMyTasks(@HeaderParam("Authorization") String authToken,
            @QueryParam("finished") boolean finished,
            @QueryParam("page") int pageNumber,
            @QueryParam("max") int pageSize) {
        sb.isAuthenticated(authToken);
        List<TaskWrapper> wrappers = new ArrayList();
        Long total = null;
        if (finished) {
            tf.findFinishedByAssignee(sb.getUser(), pageNumber, pageSize).forEach((ta) -> {
                wrappers.add(new TaskWrapper(ta, stf.isAllBegunByTask(ta)));
            });
            total = Long.valueOf(tf.getFinishedByAssigneeCount(sb.getUser()));
        } else {
            tf.findNotFinishedByAssignee(sb.getUser()).forEach((ta) -> {
                wrappers.add(new TaskWrapper(ta, stf.isAllBegunByTask(ta)));
            });
            total = Long.valueOf(wrappers.size());
        }
        InfiniteList<TaskWrapper> result = new InfiniteList();
        result.setData(wrappers);
        result.setTotal(total);
        return result;
    }

    @Path("/finish/{id}")
    @POST
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json"})
    public TaskWrapper finish(@HeaderParam("Authorization") String authToken,
            @PathParam("id") Long id,
            @QueryParam("state") TaskFinishState state,
            @QueryParam("problem") String problem) {
        sb.isAuthenticated(authToken);
        if (state != TaskFinishState.DONE && (problem == null || problem.isEmpty())) {
            throw new WebApplicationException(400);
        }
        User u = sb.getUser();
        Task t = tf.find(id);
        t.setFinishedState(state);
        t.setFinished(true);
        t.setFinishedOn(new Date());
        t.setFinshedBy(u);
        t.setProblemText(problem);
        tf.edit(t, u);
        return new TaskWrapper(t, stf.isAllBegunByTask(t));
    }

    @Path("/{id}/assign-toggle")
    @POST
    @Produces({"application/json; charset=UTF-8"})
    public TaskWrapper assignToMeToggle(@HeaderParam("Authorization") String authToken, @PathParam("id") Long id) {
        sb.isAuthenticated(authToken);
        Task t = tf.find(id);
        t.setId(id);
        User user = sb.getUser();
        t.setAssignee(user);
        tf.edit(t, user);
        return new TaskWrapper(t, stf.isAllBegunByTask(t));
    }

    @Path("/{id}")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public TaskDetailWrapper find(@HeaderParam("Authorization") String authToken, @PathParam("id") Long id) {
        sb.isAuthenticated(authToken);
        Task t = tf.find(id);
        return new TaskDetailWrapper(new TaskWrapper(t, stf.isAllBegunByTask(t)), stf.getSubtaskListCountByTask(t), tcf.getTaskCommentListCountByTask(t));
    }

    @Path("/search")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public List<TaskWrapper> search(@HeaderParam("Authorization") String authToken,
            @QueryParam("query") String query) {
        sb.isAuthenticated(authToken);
        List<TaskWrapper> wrappers = new ArrayList<>();
        tf.asyncSearchMyFinishedTasks(sb.getUser(), query).forEach((b) -> {
            wrappers.add(new TaskWrapper(b));
        });

        return wrappers;
    }

    @GET
    @Produces({"application/json; charset=UTF-8"})
    public TaskWrapper searchById(@HeaderParam("Authorization") String authToken,
            @QueryParam("id") Long id) {
        sb.isAuthenticated(authToken);
        return new TaskWrapper(tf.findMyFinishedTasksById(sb.getUser(), id));
    }

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhotoWrapper> upload(
            @HeaderParam("Authorization") String authToken,
            final FormDataMultiPart multiPart,
            @QueryParam("taskId") Long taskId) throws IOException {
        sb.isAuthenticated(authToken);
        Task t = tf.find(taskId);
        List<FormDataBodyPart> files = multiPart.getFields("image[]");
        List<PhotoWrapper> result = fs.uploadFileList(UploadType.TASK_IMAGE, t, files, sb.getUser())
                .stream().map(saved -> {
                    return new PhotoWrapper(saved);
                }).collect(Collectors.toList());
//        List<PhotoWrapper> result = new ArrayList();
//        for (FormDataBodyPart bodyPart : files) {
//            BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyPart.getEntity();
//            String name = bodyPart.getContentDisposition().getFileName();
//            try (InputStream stream = bodyPartEntity.getInputStream()) {
//                File saved = fs.uploadFile(UploadType.TASK_IMAGE, t, name, stream);
//                result.add(new PhotoWrapper(saved));
//                stream.close();
//            }
//        }
        return result;
    }

    @GET
    @Path("/photo-list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhotoWrapper> photoList(@HeaderParam("Authorization") String authToken,
            @QueryParam("taskId") Long taskId) {
        sb.isAuthenticated(authToken);
        Task t = tf.find(taskId);
        List<PhotoWrapper> result = new ArrayList<>();
        fs.listFiles(UploadType.TASK_IMAGE, t).forEach(f -> {
            result.add(new PhotoWrapper(f));
        });
        return result;
    }

    @DELETE
    @Path("/remove-photo")
    @Produces(MediaType.APPLICATION_JSON)
    public void removePhoto(@HeaderParam("Authorization") String authToken,
            @QueryParam("path") String path) throws IOException {
        sb.isAuthenticated(authToken);
        Files.delete(Paths.get(path));
    }

}
