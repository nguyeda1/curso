package com.schedek.curso.web.converter;

import com.schedek.curso.ejb.enums.BookingState;
import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@FacesConverter(forClass = BookingState.class)
@Named
@ApplicationScoped
public class BookingStateConverter  implements Converter, Serializable{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return BookingState.valueOf(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString();
	}
	
}
