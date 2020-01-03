package com.schedek.curso.web.beans.sess;

import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.web.beans.app.ApplicationBean;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.FacesUtil;

/**
 *
 * @author Root
 */
@Named("sess")
@SessionScoped
public class SessionBean implements Serializable {

    @EJB
    UserFacade uf;
    @Inject
    ApplicationBean ap;
    private User user;
    private User originalUser;
    private String encryptPassword;

    /**
     * s
     * Creates a new instance of SessionBean
     */
    public SessionBean() {
    }

    public java.util.TimeZone getTimezone() {
        return java.util.TimeZone.getDefault();
    }

    public User getUser() {
        if (originalUser == null) {
            return getUserServlet((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
        }
        return user;
    }

    public User getUserServlet(HttpServletRequest req) {
        if (originalUser == null) {
            if (user != null && !user.getUsername().equals(req.getRemoteUser())) {
                user = null;
            }
            if (user == null && req.getRemoteUser() != null) {
                user = uf.findOneByUsername(req.getRemoteUser());
            }
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String changeSite() {
        return "/index.xhtml";
    }

    //impersonation support
    public User getOriginalUser() {
        return originalUser;
    }

    public void setOriginalUser(User originalUser) {
        this.originalUser = originalUser;
    }

    public String resetUser() {
        if (originalUser != null) {
            user = originalUser;
            originalUser = null;
            resetSession();
        }
        return "/index.xhtml";
    }

    public static User getCurrentUser() {
        return getSessionBean().getUser();
    }

    public static SessionBean getSessionBean() {
        return (SessionBean) FacesUtil.getInstance().getValue(FacesContext.getCurrentInstance(), "#{sess}", SessionBean.class);
    }

    public void resetSession() {
        Map<String, Object> sm = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        for (Map.Entry<String, Object> ro : sm.entrySet()) {
            Object o = ro.getValue();
            try {
                Method m = o.getClass().getMethod("getInstance", new Class[]{});
                Object so = m.invoke(o, new Object[]{});
                if (so instanceof SessionBean) {
                    continue;
                }
            } catch (Exception ex) {

            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(ro.getKey());
        }
    }

    public String getTarget() {
        String target = "";
        if (getUser() != null) {
            target = "/app/booking/index.xhtml";
//            if (user.isAdmin() || user.isListingsManager()) {
//                target = "/app/booking/index.xhtml";
//            }
//            if (user.isSales() || user.isInternalSales()) {
//                target = "/app/airroom/index.xhtml";
//            }
//            if (user.isCleaning()) {
//                target = "/app/cleaning/dashboard.xhtml";
//            }
//            if (user.isOperator() || user.isReception()) {
//                target = "/app/booking/index.xhtml";
//            }
//            if (user.isSupplyCheck()) {
//                target = "/app/supply_check/dashboard.xhtml";
//            }
//            if (user.isReception()) {
//                target = "/app/booking/index.xhtml";
//            }
//            if (user.isWarehouse()) {
//                target = "/app/supply_check/supply_check_list.xhtml";
//            }
//            if (user.isAccountantInternal()) {
//                target = "/app/invoicein/index.xhtml?state=DRAFT";
//            }
//            if (user.isHrSupport()) {
//                target = "/app/user/index.xhtml";
//            }
        }
        return target;
    }

    public void loadHome() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        //ctx.renderResponse();
        String cp = ctx.getExternalContext().getRequestContextPath();
        try {
            ctx.getExternalContext().redirect(cp + getTarget());
            ctx.responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(SessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword;
    }
    
    public String logout() {
        user = null;
        originalUser = null;
        ap.logout();
        return null;
    }
}
