package com.schedek.curso.web.beans.app.casetag;

import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.facade.CaseTagFacade;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "caseTagEditBean")
@ViewScoped
public class CaseTagEditBean implements Serializable{

	@Inject
	private SessionBean sb;
	@EJB
	CaseTagFacade cf;
	private CaseTag c;
	private String caseTagID;

	public CaseTagEditBean() {
	}

	public void init() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			if (caseTagID != null) {
				c = cf.find(Long.parseLong(caseTagID));
				if (c == null) {
					throw new UnsupportedOperationException();
				}
			}
			if (c == null) {
				c = new CaseTag();
			}
		}
	}

	public String getCaseTagID() {
		return caseTagID;
	}

	public void setCaseTagID(String caseTagID) {
		this.caseTagID = caseTagID;
	}

	public CaseTag getC() {
		return c;
	}

	public String finish() {
		save();
		return "index.xhtml?faces-redirect=true" ;
	}
	public String save() {
		if (c.getId() == null) {
			cf.create(c);
		} else {
			cf.edit(c);
		}
		return "edit.xhtml?faces-redirect=true&id=" + c.getId() + "";
	}
}
