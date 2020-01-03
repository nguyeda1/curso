package com.schedek.curso.web.converter;

import com.schedek.curso.ejb.entities.Group;
import com.schedek.curso.ejb.facade.AbstractFacade;
import com.schedek.curso.ejb.facade.GroupFacade;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@FacesConverter(forClass = Group.class)
@Named
@ApplicationScoped
public class GroupConverter extends AbstractJPAConverter{
	@EJB GroupFacade facade;

	@Override
    protected AbstractFacade getFacade(FacesContext ctx) {
		return (ctx.getApplication().evaluateExpressionGet(ctx, "#{groupConverter}", GroupConverter.class)).facade;
    }

}
