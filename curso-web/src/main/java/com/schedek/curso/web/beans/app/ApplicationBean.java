/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.web.beans.app;

import com.schedek.curso.ejb.AppEJB;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Root
 */
@Named(value = "app")
@ApplicationScoped
public class ApplicationBean {

	@EJB
	AppEJB app;
	private Locale locale;

	/**
	 * Creates a new instance of ApplicationBean
	 */
	public ApplicationBean() {
	}

	public String getBuildDate() {
		return app.getBuildDate();
	}

	public String getVersion() {
		return app.getVersion();
	}

	public boolean isDev() {
		return app.getConfig().isDev();
	}

	public boolean isProduction() {
		return app.isProduction();
	}

	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try {
			request.logout();
		} catch (ServletException ex) {
			Logger.getLogger(ApplicationBean.class.getName()).log(Level.SEVERE, null, ex);
		}
		HttpServletResponse resp = (HttpServletResponse) context.getExternalContext().getResponse();
		try {
			resp.sendRedirect(request.getContextPath());
			context.responseComplete();
		} catch (IOException ex) {
			Logger.getLogger(ApplicationBean.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public Locale getLocale() {
		if (locale == null) {
			locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
		}
		return locale;
	}

	public String formatDate(Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");
		return df.format(d);
	}

	public String formatDateShort(Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("d.M.");
		return df.format(d);
	}

	public String formatTimeEstimate(long min) {
		String result = "";
		result = min % 60 + "m";
		if (min > 60) {
			result = ((min - min % 60) / 60) + "h " + result;
		}
		return result;
	}

	public String formatDateShortYear(Date d) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(d);
	}

	public String getPrefix() {
		return app.getConfig().getPrefix();
	}

	public String getTaskmanagerUrl() {
		return app.getConfig().getTaskmanagerUrl();
	}

	public Date getNow() {
		return new Date();
	}

}
