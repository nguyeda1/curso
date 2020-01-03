package com.schedek.curso.web.beans.app.currency;

import com.schedek.curso.ejb.entities.Currency;
import com.schedek.curso.web.beans.list.AbstractListState;
import javax.inject.Named;
import java.util.List;
import javax.faces.view.ViewScoped;

@Named(value = "currencyListState")
@ViewScoped
public class CurrencyListState extends AbstractListState{

	private List<Currency> filteredValue;

	public CurrencyListState() {
	}

	public List<Currency> getFilteredValue() {
		return filteredValue;
	}

	public  void setFilteredValue(List<Currency> filteredValue) {
		this.filteredValue = filteredValue;
	}
	
	@Override
	public  void setFilteredValueInt(List filteredValue) {
		this.filteredValue = filteredValue;
	}
}
