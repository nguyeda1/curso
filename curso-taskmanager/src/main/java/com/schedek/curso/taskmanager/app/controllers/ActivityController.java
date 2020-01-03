package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Activity;
import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseActivity;
import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.TaskActivity;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.SubtaskState;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.taskmanager.app.beans.SessionBean;
import com.schedek.curso.taskmanager.app.dto.InfiniteList;
import com.schedek.curso.taskmanager.app.dto.activity.ActivityWrapper;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Dan Nguyen
 */
@Path("/activity")
@ApplicationScoped
@Named
public class ActivityController {

    @Inject
    SessionBean sb;
    @EJB
    ActivityFacade af;
    @EJB
    CaseFacade cf;
    @EJB
    UserFacade uf;
    @EJB
    ListingFacade lf;

    @Path("/init")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public InfiniteList<ActivityWrapper> findByCase(@HeaderParam("Authorization") String authToken,
            @QueryParam("caseId") Long caseId,
            @QueryParam("page") int pageNumber,
            @QueryParam("max") int pageSize) {
        sb.isAuthenticated(authToken);
        User u = sb.getUser();
        Case c = caseId != null ? cf.find(caseId) : null;
        List<ActivityWrapper> wrappers = new ArrayList<>();
        List<Activity> list;
        Long count;
        if (c != null) {
            list = af.findByCaseAndPage(c, pageNumber, pageSize);
            count = Long.valueOf(af.getActivityCountByCase(c));
        } else {
            list = af.findByInterestedParties(u, pageNumber, pageSize);

            count = Long.valueOf(af.getActivityCountByInterestedParties(u));
        }
        list.forEach(a -> {
            if (a instanceof CaseActivity) {
                wrappers.add(new ActivityWrapper((CaseActivity) a));
            } else if (a instanceof TaskActivity) {
                wrappers.add(new ActivityWrapper((TaskActivity) a));
            }
        });
        InfiniteList result = new InfiniteList(wrappers, count);
        if (c == null) {
            Date lastRead = new Date(u.getLastActivityRead().getTime());
            u.setLastActivityRead(new Date());
            u.setLastActivityFetch(new Date());
            uf.edit(u);
            result.setLastRead(lastRead);
        }
        return result;
    }

    @Path("/refresh")
    @GET
    @Produces({"application/json; charset=UTF-8"})
    public InfiniteList<ActivityWrapper> findNew(@HeaderParam("Authorization") String authToken,
            @QueryParam("firstFetch") boolean isFirst,
            @QueryParam("reading") boolean isReading) {
        sb.isAuthenticated(authToken);
        User u = sb.getUser();
        List<ActivityWrapper> wrappers = new ArrayList<>();
        Date date = isFirst ? u.getLastActivityRead() : u.getLastActivityFetch();
        af.findNew(date, u).forEach((a) -> {
            wrappers.add(new ActivityWrapper(a));
        });
        u.setLastActivityFetch(new Date());
        Date d = new Date(u.getLastActivityRead().getTime());
        if (isReading) {
            u.setLastActivityRead(new Date());
        }
        uf.edit(u);
        InfiniteList result = new InfiniteList(wrappers, Long.valueOf(af.getActivityCountByInterestedParties(u)));
        result.setLastRead(d);
        return result;
    }
}
