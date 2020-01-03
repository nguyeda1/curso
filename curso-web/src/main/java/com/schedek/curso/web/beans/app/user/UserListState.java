package com.schedek.curso.web.beans.app.user;

import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.web.beans.list.AbstractListState;
import javax.inject.Named;
import java.util.List;
import javax.faces.view.ViewScoped;

@Named(value = "userListState")
@ViewScoped
public class UserListState extends AbstractListState{

	private List<User> filteredValue;

	public UserListState() {
	}

	public List<User> getFilteredValue() {
		return filteredValue;
	}

	public  void setFilteredValue(List<User> filteredValue) {
		this.filteredValue = filteredValue;
	}
	
	@Override
	public  void setFilteredValueInt(List filteredValue) {
		this.filteredValue = filteredValue;
	}
}
