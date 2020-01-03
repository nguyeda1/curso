package com.schedek.curso.web.beans.app.currency;

import com.schedek.curso.ejb.entities.Currency;
import com.schedek.curso.ejb.facade.CurrencyFacade;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "currencyEditBean")
@ViewScoped
public class CurrencyEditBean implements Serializable{

	@Inject
	private SessionBean sb;
	@EJB
	CurrencyFacade cf;
	private Currency c;
	private String currencyID;

	public CurrencyEditBean() {
	}

	public void init() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			if (currencyID != null) {
				c = cf.find(currencyID);
				if (c == null) {
					throw new UnsupportedOperationException();
				}
			}
			if (c == null) {
				c = new Currency();
			}
		}
	}

	public String getCurrencyID() {
		return currencyID;
	}

	public void setCurrencyID(String currencyID) {
		this.currencyID = currencyID;
	}

	public Currency getC() {
		return c;
	}

	public String finish() {
		save();
		return "index.xhtml?faces-redirect=true" ;
	}
	public String save() {
		if (c.getCode()== null) {
			cf.create(c);
		} else {
			cf.edit(c);
		}
		return "edit.xhtml?faces-redirect=true&id=" + c.getCode()+ "";
	}
}
