/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.BookingFacade;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.CaseFeedbackFacade;
import com.schedek.curso.ejb.facade.CaseTagFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.ejb.facade.TaskFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.ejb.files.Filestore;
import com.schedek.curso.ejb.files.UploadType;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.InfiniteList;
import com.schedek.curso.taskmanager.app.dto.PhotoListWrapper;
import com.schedek.curso.taskmanager.app.dto.PhotoWrapper;
import com.schedek.curso.taskmanager.app.dto.cases.CaseDetailWrapper;
import com.schedek.curso.taskmanager.app.dto.cases.CaseWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author Dan Nguyen
 */
@Path("/case")
@ApplicationScoped
@Named
public class CaseController {

    @Inject
    SessionBean sb;
    @EJB
    Filestore fs;
    @EJB
    CaseFacade cf;
    @EJB
    CaseFeedbackFacade crf;
    @EJB
    CaseTagFacade ctf;
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

    @Path("/init")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public InfiniteList<CaseWrapper> list(@HeaderParam("Authorization") String authToken,
            @QueryParam("page") int pageNumber,
            @QueryParam("max") int pageSize,
            @QueryParam("filter") String filter) {
        sb.isAuthenticated(authToken);
        List<CaseWrapper> wrappers = new ArrayList<>();
        Long count;
        if (filter.equals("MY_CASES")) {
            User u = sb.getUser();
            cf.findMyUnfinishedCases(u, pageNumber, pageSize).forEach(c -> wrappers.add(new CaseWrapper(c)));
            count = cf.getMyUnfinishedCasesCount(u);
        } else {
            CaseState state = filter.equals("ALL") ? null : CaseState.valueOf(filter);
            cf.findByStateAndPage(state, pageNumber, pageSize).forEach(c -> wrappers.add(new CaseWrapper(c)));
            count = cf.getCaseListCountByState(state);
        }
        return new InfiniteList(wrappers, count);
    }

    @Path("/search")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public List<CaseWrapper> search(@HeaderParam("Authorization") String authToken,
            @QueryParam("filter") String filter,
            @QueryParam("query") String query) {
        sb.isAuthenticated(authToken);
        List<CaseWrapper> wrappers = new ArrayList<>();
        if (filter.equals("MY_CASES")) {
            User u = sb.getUser();
            cf.asyncSearchMyCases(u, query).forEach((b) -> {
                wrappers.add(new CaseWrapper(b));
            });
        } else {
            CaseState state = filter.equals("ALL") ? null : CaseState.valueOf(filter);
            cf.asyncSearch(state, query).forEach((b) -> {
                wrappers.add(new CaseWrapper(b));
            });
        }

        return wrappers;
    }

    @GET
    @Produces({"application/json; charset=UTF-8"})
    public CaseWrapper searchById(@HeaderParam("Authorization") String authToken,
            @QueryParam("filter") String filter,
            @QueryParam("id") Long id) {
        sb.isAuthenticated(authToken);
        CaseWrapper res;
        if (filter.equals("MY_CASES")) {
            User u = sb.getUser();
            res = new CaseWrapper(cf.findMyCasesById(u, id));
        } else {
            CaseState state = filter.equals("ALL") ? null : CaseState.valueOf(filter);
            res = new CaseWrapper(cf.findByIdAndState(state, id));
        }

        return res;
    }

    @Path("/{id}")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public CaseDetailWrapper getDetail(@HeaderParam("Authorization") String authToken, @PathParam("id") Long id) {
        sb.isAuthenticated(authToken);
        Case c = cf.find(id);
        return new CaseDetailWrapper(c, tf.getTaskListCountByCriteria(c, null));
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json; charset=UTF-8"})
    @Transactional
    public CaseWrapper update(@HeaderParam("Authorization") String authToken, CaseWrapper object) {
        sb.isAuthenticated(authToken);
        User user = sb.getUser();
        Case result = object.getId() == null ? new Case() : cf.find(object.getId());

        result.setListing(object.getListing() != null
                ? lf.find(object.getListing().getId())
                : null);
        result.setBooking((object.getBooking() != null
                && object.getListing().equals(object.getBooking().getListing()))
                ? bf.find(object.getBooking().getId())
                : null);
        result.setOwner(object.getOwner() != null
                ? uf.find(object.getOwner().getId())
                : null);
        result.setName(object.getName());
        result.setDescription(object.getDescription());

        if (result.getId() == null) {
            result.setCaseState(CaseState.NEW);
            result.setCreatedOn(new Date());
            result.setCreatedBy(user);
            result.setOwner(result.getOwner() != null ? result.getOwner() : user);
            cf.create(result, user);
        } else {
            cf.edit(result, user);
        }
        return new CaseWrapper(result);
    }

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhotoWrapper> upload(
            @HeaderParam("Authorization") String authToken,
            final FormDataMultiPart multiPart,
            @QueryParam("caseId") Long caseId) throws IOException {
        sb.isAuthenticated(authToken);
        Case c = cf.find(caseId);
        List<FormDataBodyPart> files = multiPart.getFields("image[]");
        List<PhotoWrapper> result = fs.uploadFileList(UploadType.CASE_IMAGE, c, files, sb.getUser())
                .stream().map(saved -> {
                    return new PhotoWrapper(saved);
                }).collect(Collectors.toList());
        return result;
    }

    @GET
    @Path("/photo-list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhotoWrapper> photoList(@HeaderParam("Authorization") String authToken,
            @QueryParam("caseId") Long caseId) {
        sb.isAuthenticated(authToken);
        Case c = cf.find(caseId);
        List<PhotoWrapper> result = new ArrayList<>();
        fs.listFiles(UploadType.CASE_IMAGE, c).forEach(f -> {
            result.add(new PhotoWrapper(f));
        });
        return result;
        //return new PhotoListWrapper(fs.listFiles(UploadType.CASE_IMAGE, c));
    }

    @DELETE
    @Path("/remove-photo")
    @Produces(MediaType.APPLICATION_JSON)
    public void removePhoto(@HeaderParam("Authorization") String authToken,
            @QueryParam("path") String path) throws IOException {
        sb.isAuthenticated(authToken);
        Files.delete(Paths.get(path));
    }

    @POST
    @Path("/{id}/follow")
    @Produces(MediaType.APPLICATION_JSON)
    public CaseWrapper followToggle(@HeaderParam("Authorization") String authToken,
            @PathParam("id") Long id) {
        sb.isAuthenticated(authToken);
        User user = sb.getUser();
        Case c = cf.find(id);
        if (c != null) {
            if (!c.getFollowers().contains(user)) {
                c.getFollowers().add(user);
            } else {
                c.getFollowers().remove(user);
            }
            cf.followLog(c, user);
        }
        return new CaseWrapper(c);
    }

}
