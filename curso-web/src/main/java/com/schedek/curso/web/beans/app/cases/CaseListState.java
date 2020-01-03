package com.schedek.curso.web.beans.app.cases;


import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.web.beans.list.AbstractListState;
import javax.inject.Named;
import java.util.List;
import javax.enterprise.context.SessionScoped;

@Named(value = "caseListState")
@SessionScoped
public class CaseListState extends AbstractListState{

	private List<Case> filteredValue;

	public CaseListState() {
	}

	public List<Case> getFilteredValue() {
		return filteredValue;
	}

	public  void setFilteredValue(List<Case> filteredValue) {
		this.filteredValue = filteredValue;
	}
	
	@Override
	public  void setFilteredValueInt(List filteredValue) {
		this.filteredValue = filteredValue;
	}
}
