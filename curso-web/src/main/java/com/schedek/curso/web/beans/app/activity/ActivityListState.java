package com.schedek.curso.web.beans.app.activity;




import com.schedek.curso.ejb.entities.Activity;
import com.schedek.curso.web.beans.list.AbstractListState;
import javax.inject.Named;
import java.util.List;
import javax.enterprise.context.SessionScoped;

@Named(value = "activityListState")
@SessionScoped
public class ActivityListState extends AbstractListState{

	private List<Activity> filteredValue;

	public ActivityListState() {
	}

	public List<Activity> getFilteredValue() {
		return filteredValue;
	}

	public  void setFilteredValue(List<Activity> filteredValue) {
		this.filteredValue = filteredValue;
	}
	
	@Override
	public  void setFilteredValueInt(List filteredValue) {
		this.filteredValue = filteredValue;
	}
}
