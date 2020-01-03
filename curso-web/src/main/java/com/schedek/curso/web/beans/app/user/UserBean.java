package com.schedek.curso.web.beans.app.user;

import com.schedek.curso.ejb.entities.Group;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.GroupFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.web.beans.list.JPADataModel;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.naming.NamingException;
import org.glassfish.hk2.utilities.reflection.Logger;
import org.primefaces.model.LazyDataModel;
import util.FacesUtil;

@Named(value = "userBean")
@ViewScoped
public class UserBean implements Serializable {

    @EJB
    UserFacade ef;
    @EJB
    GroupFacade gf;
    @Inject
    private SessionBean sb;
    LazyDataModel<User> users;
    private Group filterGroup;
    private boolean showDisabled;
    private List<User> userList;

    public UserBean() {
    }

    public LazyDataModel<User> getUsers() {
        if (users == null) {
            users = new JPADataModel(ef) {

                @Override
                protected QueryBuilder getQueryBuilder() {
                    return ef.qbFilterAll(filterGroup == null ? null : Arrays.asList(new Group[]{filterGroup}));
                }

            };
        }
        return users;
    }

    public List<User> getUserList() {
        if (userList == null) {
            userList = ef.qbFilterAll(null).loadAll();
        }
        return userList;
    }


    public User getDelete() {
        return null;
    }

    public void setDelete(User delete) {
        try {
            ef.remove(delete);
        } catch (Exception e) {
            FacesUtil.processDbException("Delete failed", e);
        }
    }

}
