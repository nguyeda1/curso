package com.schedek.curso.web.converter;

import com.schedek.curso.ejb.facade.AbstractFacade;
import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

abstract class AbstractJPAConverter<T> implements Converter, Serializable {

	public static final String NO_OPTION_TEXT = "(not selected)";

	protected abstract AbstractFacade<T> getFacade(FacesContext context);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (NO_OPTION_TEXT.equals(value) || value.isEmpty()) {
			return null;
		}
		AbstractFacade f = getFacade(context);
		Class idc = f.getIDClass();
		if (Long.class.equals(idc)) {
			return f.find(Long.parseLong(value));
		} else if (String.class.equals(idc)) {
			return f.find(value);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return NO_OPTION_TEXT;
		}
		Object pk = getFacade(context).getID((T) value);
		if (pk instanceof Long) {
			return ((Long) pk).toString();
		} else if (pk instanceof String) {
			return (String) pk;
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
