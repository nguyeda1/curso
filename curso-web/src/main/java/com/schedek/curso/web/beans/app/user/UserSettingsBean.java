package com.schedek.curso.web.beans.app.user;

import com.schedek.curso.web.beans.common.AuthBean;
import com.schedek.curso.ejb.AppEJB;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.naming.NamingException;

@Named(value = "userSettingsBean")
@ViewScoped
public class UserSettingsBean extends AuthBean implements Serializable {

    @Inject
    private SessionBean sb;
    @EJB
    UserFacade cf;
    @EJB
    AppEJB app;
    private User c;
    private String userID;

    private String oldPassword;
    private String newPassword;

    public UserSettingsBean() {
    }

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (userID != null) {
                User u = cf.find(Long.parseLong(userID));
                if (sb.getUser().equals(u) || sb.getUser().isAdmin()) {
                    this.c = u;
                }
                if (c == null) {
                    throw new UnsupportedOperationException();
                }
            } else {
                throw new UnsupportedOperationException();
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void passwordChange() {
        if (c.isPasswordValid(oldPassword)) {
            c.setPassword(newPassword);
            save();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Old pasword mismatch"));
        }
    }

    public void save() {
        sb.setUser(c);
//        if (c.getAirLogin() != null && c.getAirPassword() != null) {
//            try {
//                aab.checkAuthentication(null, c, authentication);
//                
//                if(authentication.isStatus(AuthenticationStatus.SUCCESS)){
//                    AirUser au = aab.getActiveAirUser(null, c);
//                    c.setAirUser(au);
//                }
//                
//                if(authentication.isStatus(AuthenticationStatus.FAILED)){
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "AirAPI Error", "Error getting user info, check credentials"));
//                    return;
//                }
//                
//                if(authentication.isStatus(AuthenticationStatus.AIRLOCK)){
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "AirBnb", "AirBnb requires authentication via email or sms"));
//                    return;
//                }
//            } catch (Exception ex) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "AirAPI Error", "Error getting user info, check credentials"));
//                Logger.getLogger(UserSettingsBean.class.getName()).log(Level.SEVERE, "Error editing user settings", ex);
//                return;
//            }
//        }
        String feedback = "";

        if (c.getId() == null) {
            cf.create(c);
            feedback = "Create successful";
        } else {
            cf.edit(c);
            feedback = "Edit successful";
        }
        cf.flush();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(feedback));
    }

    public void airAuthorize() {
        sb.setUser(c);

//		if (authentication.isStatus(AuthenticationStatus.SUCCESS)) {
//			if (c.getId() == null) {
//				cf.create(c);
//			} else {
//				cf.edit(c);
//			}
//			cf.flush();
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Synchronization", "Successful"));
//		}
//
//		if (authentication.isStatus(AuthenticationStatus.ERROR)) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Synchronization", "Failed"));
//		}
    }

}
