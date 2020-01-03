/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.web.beans.app.locality;

import com.schedek.curso.ejb.entities.Locality;
import com.schedek.curso.ejb.facade.LocalityFacade;
import com.schedek.curso.web.beans.list.JPADataModel;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import util.FacesUtil;

/**
 *
 * @author Dan Nguyen
 */
@Named(value = "localityBean")
@ViewScoped
public class LocalityBean implements Serializable {

    @EJB
    LocalityFacade lf;
    ArrayList<Locality> localities;

    public LocalityBean() {
    }

    public ArrayList<Locality> getLocalities() {
        if (localities == null) {
            localities = new ArrayList<>();
            for (Locality locality : lf.findAll()) {
                localities.add(locality);
            }
        }
        return localities;
    }

    public Locality getDelete() {
        return null;
    }

    public void setDelete(Locality delete) {
        try {
            lf.remove(delete);
        } catch (Exception e) {
            FacesUtil.processDbException("Delete failed", e);
        }
    }

}
