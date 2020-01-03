package com.schedek.curso.web.converter;

import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.AbstractFacade;
import com.schedek.curso.ejb.facade.UserFacade;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@FacesConverter(forClass = User.class)
@Named
@ApplicationScoped
public class UserConverter extends AbstractJPAConverter {
    @EJB
    UserFacade facade;

    @Override
    protected AbstractFacade getFacade(FacesContext ctx) {
        return (ctx.getApplication().evaluateExpressionGet(ctx, "#{userConverter}", UserConverter.class)).facade;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if ("0".equals(value)) {
            User u = new User();
            u.setId(0L);
            return u;
        }
        return super.getAsObject(context, component, value);
    }
}
