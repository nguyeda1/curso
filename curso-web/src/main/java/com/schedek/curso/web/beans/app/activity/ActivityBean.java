package com.schedek.curso.web.beans.app.activity;

import com.schedek.curso.ejb.entities.Activity;
import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseActivity;
import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.entities.TaskActivity;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.ejb.view.ViewActivity;
import com.schedek.curso.web.beans.app.user.UserBean;
import com.schedek.curso.web.beans.list.JPADataModel;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.Serializable;
import java.util.*;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import util.FacesUtil;

@Named(value = "activityBean")
@ViewScoped
public class ActivityBean implements Serializable {

    @EJB
    ActivityFacade ef;
    @EJB
    UserFacade usf;
    LazyDataModel<Activity> activities;
    @Inject
    SessionBean sb;
    private User user;

    public ActivityBean() {
    }
    
    public void init(){
        user = usf.find(sb.getUser().getId());
    }

    public LazyDataModel<Activity> getActivity() {
        if (activities == null) {
            activities = new JPADataModel<Activity>(ef){
                @Override
                protected QueryBuilder getQueryBuilder() {
                   return ef.qbActivityByInterestedParties(user);
                }
                
            };
        }
        return activities;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TaskActivity toTaskActivity(Activity a) {
        if (!(a instanceof TaskActivity)) {
            return null;
        }
        return (TaskActivity) a;
    }
    
    public CaseActivity toCaseActivity(Activity a) {
        if (!(a instanceof CaseActivity)) {
            return null;
        }
        return (CaseActivity) a;
    }

}
