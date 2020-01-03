/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.web.converter;

import com.schedek.curso.ejb.entities.Locality;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.AbstractFacade;
import com.schedek.curso.ejb.facade.LocalityFacade;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

/**
 *
 * @author Dan Nguyen
 */
@FacesConverter(forClass = Locality.class)
@Named
@ApplicationScoped
public class LocalityConverter extends AbstractJPAConverter {

    @EJB
    LocalityFacade facade;

    @Override
    protected AbstractFacade getFacade(FacesContext ctx) {
        return (ctx.getApplication().evaluateExpressionGet(ctx, "#{localityConverter}", LocalityConverter.class)).facade;
    }
}
