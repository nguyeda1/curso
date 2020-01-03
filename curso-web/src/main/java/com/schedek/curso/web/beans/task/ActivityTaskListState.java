/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.web.beans.task;

import com.schedek.curso.ejb.entities.TaskActivity;
import com.schedek.curso.web.beans.list.AbstractListState;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "activityTaskListState")
@ViewScoped
public class ActivityTaskListState extends AbstractListState{

	private List<TaskActivity> filteredValue;

	public ActivityTaskListState() {
	}

	public List<TaskActivity> getFilteredValue() {
		return filteredValue;
	}

	public  void setFilteredValue(List<TaskActivity> filteredValue) {
		this.filteredValue = filteredValue;
	}
	
	@Override
	public  void setFilteredValueInt(List filteredValue) {
		this.filteredValue = filteredValue;
	}
}