package com.schedek.curso.web.beans.app.listing;

import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.web.beans.list.AbstractListState;
import javax.inject.Named;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;

@Named(value = "listingListState")
@SessionScoped
public class ListingListState extends AbstractListState{

	private List<Listing> filteredValue;

	public ListingListState() {
	}

	public List<Listing> getFilteredValue() {
		return filteredValue;
	}

	public  void setFilteredValue(List<Listing> filteredValue) {
		this.filteredValue = filteredValue;
	}
	
	@Override
	public  void setFilteredValueInt(List filteredValue) {
		this.filteredValue = filteredValue;
	}
}
