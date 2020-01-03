package com.schedek.curso.web.beans.app.user;

import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.naming.NamingException;

@Named(value = "userEditBean")
@ViewScoped
public class UserEditBean implements Serializable {

    @Inject
    private SessionBean sb;
    @EJB
    UserFacade cf;
    private User c;
    private String userID;

    public UserEditBean() {
    }

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (userID != null) {
                c = cf.find(Long.parseLong(userID));
                if (c == null) {
                    throw new UnsupportedOperationException();
                }
            }
            if (c == null) {
                c = new User();
            }
        }
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public User getC() {
        return c;
    }

    public String finish() {
        save();
        return "index.xhtml?faces-redirect=true";
    }
    
    public boolean newPasswordRequired(){
        
        if(c.getId() != null) return false;
        
        return true;
    }

    public String save() {
        if (!sb.getUser().isAdmin() && c.isAdmin()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Only admin can create admin users"));
            return "";
        }
        if(c.getId() != null && cf.find(c.getId()).getGroups().isEmpty() && !c.getGroups().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Password is required"));
                return "";
        }
    
        
        if (c.getId() == null) {
            String username = cf.createUsername(c);
            if (username == null || username.trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Username is empty"));
                return "";
            }
            c.setUsername(username);
            cf.create(c);
        } else {
            cf.edit(c);
        }
        
        return "edit.xhtml?faces-redirect=true&id=" + c.getId() + "";
    }
}
