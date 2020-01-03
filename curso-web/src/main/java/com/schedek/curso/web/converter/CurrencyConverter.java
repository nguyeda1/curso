package com.schedek.curso.web.converter;

import com.schedek.curso.ejb.entities.Currency;
import com.schedek.curso.ejb.facade.AbstractFacade;
import com.schedek.curso.ejb.facade.CurrencyFacade;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@FacesConverter(forClass = Currency.class)
@Named
@ApplicationScoped
public class CurrencyConverter extends AbstractJPAConverter{
	@EJB CurrencyFacade facade;

	@Override
    protected AbstractFacade getFacade(FacesContext ctx) {
		return (ctx.getApplication().evaluateExpressionGet(ctx, "#{currencyConverter}", CurrencyConverter.class)).facade;
    }

}
