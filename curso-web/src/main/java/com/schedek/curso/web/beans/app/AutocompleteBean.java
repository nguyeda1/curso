package com.schedek.curso.web.beans.app;

import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.UserFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

@Named(value = "autocomplete")
@ApplicationScoped
public class AutocompleteBean {
	private static final int AUTOCOMPLETE_LIMIT=10;

	@EJB UserFacade uf;
	
	public AutocompleteBean() {
	}
	public List<User> user(String query) {
		return uf.findByName(query, AUTOCOMPLETE_LIMIT);
	}
	public List<User> activeUser(String query) {
		return uf.findByNameOnlyActive(query, AUTOCOMPLETE_LIMIT);
	}
}
