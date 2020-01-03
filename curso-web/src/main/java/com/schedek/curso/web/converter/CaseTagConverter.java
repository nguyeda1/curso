package com.schedek.curso.web.converter;

import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.facade.AbstractFacade;
import com.schedek.curso.ejb.facade.CaseTagFacade;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@FacesConverter(forClass = CaseTag.class)
@Named
@ApplicationScoped
public class CaseTagConverter extends AbstractJPAConverter{
	@EJB CaseTagFacade facade;

	@Override
    protected AbstractFacade getFacade(FacesContext ctx) {
		return (ctx.getApplication().evaluateExpressionGet(ctx, "#{caseTagConverter}", CaseTagConverter.class)).facade;
    }

}
