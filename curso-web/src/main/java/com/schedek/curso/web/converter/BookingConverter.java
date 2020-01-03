package com.schedek.curso.web.converter;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.facade.AbstractFacade;
import com.schedek.curso.ejb.facade.BookingFacade;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@FacesConverter(forClass = Booking.class)
@Named
@ApplicationScoped
public class BookingConverter extends AbstractJPAConverter{
	@EJB BookingFacade facade;

	@Override
    protected AbstractFacade getFacade(FacesContext ctx) {
		return (ctx.getApplication().evaluateExpressionGet(ctx, "#{bookingConverter}", BookingConverter.class)).facade;
    }

}
