package com.schedek.curso.web.beans.sess;

import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.UserFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import util.FacesUtil;

@Named("impersonation")
@ViewScoped
public class ImpersonationBean implements Serializable {

	@EJB
	UserFacade uf;
	private User user;

	public ImpersonationBean() {
	}

	public String impersonate() {
		if (user != null) {
			SessionBean sb = SessionBean.getSessionBean();
			if(sb.getOriginalUser()==null){
				sb.setOriginalUser(sb.getUser());
			}
			sb.setUser(user);
			sb.resetSession();
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User doesn't exist"));
			return null;
		}
		return "/index.xhtml";
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
