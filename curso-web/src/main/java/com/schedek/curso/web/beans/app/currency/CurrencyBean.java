package com.schedek.curso.web.beans.app.currency;

import com.schedek.curso.ejb.entities.Currency;
import com.schedek.curso.ejb.facade.CurrencyFacade;
import com.schedek.curso.web.beans.list.JPADataModel;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.LazyDataModel;
import util.FacesUtil;

@Named(value = "currencyBean")
@ViewScoped
public class CurrencyBean implements Serializable {

    @EJB
    CurrencyFacade ef;
    LazyDataModel<Currency> currencys;
    List<Currency> currenciesList;

    private Date ratesDate = new Date();

    public CurrencyBean() {
    }

    public LazyDataModel<Currency> getCurrencys() {
        if (currencys == null) {
            currencys = new JPADataModel<>(ef);
        }
        return currencys;
    }

    public Currency getDelete() {
        return null;
    }

    public void setDelete(Currency delete) {
        try {
            ef.remove(delete);
        } catch (Exception e) {
            FacesUtil.processDbException("Delete failed", e);
        }
    }

    public List<Currency> getCurrenciesList() {
        if (currenciesList == null) {
            currenciesList = ef.findAll();
        }
        return currenciesList;
    }

//        public String dailyRatesJSON(Currency c, Date d) {
//		Map<String, String> rmap = new HashMap<>();
//		javax.json.JsonObjectBuilder ob = javax.json.Json.createObjectBuilder();
//		for (Currency_rate r : crf.findRates(c, d)) {
//			if (r.getFxPair() == null || r.getFxPair().getCounter() == null || r.getFxPair().getCounter().getCode() == null) {
//				continue;
//			}
//			ob.add(r.getFxPair().getCounter().getCode(), r.getExchangeRate());
//		}
//		return ob.build().toString();
//	}
    public Date getRatesDate() {
        return ratesDate;
    }

    public void setRatesDate(Date ratesDate) {
        this.ratesDate = ratesDate;
    }

}
