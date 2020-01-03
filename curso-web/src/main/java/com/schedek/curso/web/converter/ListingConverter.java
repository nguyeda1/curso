package com.schedek.curso.web.converter;

import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.facade.AbstractFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@FacesConverter(forClass = Listing.class)
@Named
@ApplicationScoped
public class ListingConverter extends AbstractJPAConverter{
	@EJB ListingFacade facade;

	@Override
    protected AbstractFacade getFacade(FacesContext ctx) {
		return (ctx.getApplication().evaluateExpressionGet(ctx, "#{listingConverter}", ListingConverter.class)).facade;
    }

}
