/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.web.beans.app.cases;

import com.schedek.curso.ejb.entities.CaseActivity;
import com.schedek.curso.ejb.entities.TaskActivity;
import com.schedek.curso.web.beans.list.AbstractListState;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "activityCaseListState")
@ViewScoped
public class ActivityCaseListState extends AbstractListState{

	private List<CaseActivity> filteredValue;

	public ActivityCaseListState() {
	}

	public List<CaseActivity> getFilteredValue() {
		return filteredValue;
	}

	public  void setFilteredValue(List<CaseActivity> filteredValue) {
		this.filteredValue = filteredValue;
	}
	
	@Override
	public  void setFilteredValueInt(List filteredValue) {
		this.filteredValue = filteredValue;
	}
}