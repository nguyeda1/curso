package com.schedek.curso.web.beans.app.casetag;

import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.web.beans.list.AbstractListState;
import javax.inject.Named;
import java.util.List;
import javax.faces.view.ViewScoped;

@Named(value = "caseTagListState")
@ViewScoped
public class CaseTagListState extends AbstractListState{

	private List<CaseTag> filteredValue;

	public CaseTagListState() {
	}

	public List<CaseTag> getFilteredValue() {
		return filteredValue;
	}

	public  void setFilteredValue(List<CaseTag> filteredValue) {
		this.filteredValue = filteredValue;
	}
	
	@Override
	public  void setFilteredValueInt(List filteredValue) {
		this.filteredValue = filteredValue;
	}
}
