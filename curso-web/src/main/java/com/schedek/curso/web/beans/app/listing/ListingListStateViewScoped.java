package com.schedek.curso.web.beans.app.listing;

import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.web.beans.list.AbstractListState;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.List;

@Named(value = "listingListStateViewScoped")
@ViewScoped
public class ListingListStateViewScoped extends AbstractListState {
    private List<Listing> filteredValue;

    public ListingListStateViewScoped() {
    }

    public List<Listing> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Listing> filteredValue) {
        this.filteredValue = filteredValue;
    }

    @Override
    public void setFilteredValueInt(List filteredValue) {
        this.filteredValue = filteredValue;
    }
}
