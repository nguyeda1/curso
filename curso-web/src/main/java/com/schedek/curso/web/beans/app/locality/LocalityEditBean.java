/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.web.beans.app.locality;

import com.schedek.curso.ejb.entities.Locality;
import com.schedek.curso.ejb.facade.LocalityFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Dan Nguyen
 */
@Named(value = "localityEditBean")
@ViewScoped
public class LocalityEditBean implements Serializable {

    @EJB
    LocalityFacade lf;
    private Locality c;
    private String localityId;

    public LocalityEditBean() {
    }

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (localityId != null) {
                c = lf.find(Long.parseLong(localityId));
                if (c == null) {
                    throw new UnsupportedOperationException();
                }
            }
            if (c == null) {
                c = new Locality();
            }
        }
    }

    public Locality getC() {
        return c;
    }

    public void setC(Locality c) {
        this.c = c;
    }

    public String getLocalityId() {
        return localityId;
    }

    public void setLocalityId(String localityId) {
        this.localityId = localityId;
    }
    
    public String finish() {
        save();
        return "index.xhtml?faces-redirect=true";
    }

    public String save() {
        if (c.getId() == null) {
            lf.create(c);
        } else {
            lf.edit(c);
        }
        return "edit.xhtml?faces-redirect=true&id=" + c.getId() + "";
    }

}
