package com.schedek.curso.web.beans.task;

import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.web.beans.list.AbstractListState;
import javax.inject.Named;
import java.util.List;
import javax.enterprise.context.SessionScoped;

@Named(value = "taskListState")
@SessionScoped
public class TaskListState extends AbstractListState{

	private List<Task> filteredValue;

	public TaskListState() {
	}

	public List<Task> getFilteredValue() {
		return filteredValue;
	}

	public  void setFilteredValue(List<Task> filteredValue) {
		this.filteredValue = filteredValue;
	}
	
	@Override
	public  void setFilteredValueInt(List filteredValue) {
		this.filteredValue = filteredValue;
	}
}
