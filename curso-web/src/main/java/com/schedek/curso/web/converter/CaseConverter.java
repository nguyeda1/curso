package com.schedek.curso.web.converter;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.facade.AbstractFacade;
import com.schedek.curso.ejb.facade.CaseFacade;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@FacesConverter(forClass = Case.class)
@Named
@ApplicationScoped
public class CaseConverter extends AbstractJPAConverter{
	@EJB CaseFacade facade;

	@Override
    protected AbstractFacade getFacade(FacesContext ctx) {
		return (ctx.getApplication().evaluateExpressionGet(ctx, "#{caseConverter}", CaseConverter.class)).facade;
    }

}