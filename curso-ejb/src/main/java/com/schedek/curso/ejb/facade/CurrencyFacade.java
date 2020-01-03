package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Currency;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CurrencyFacade extends AbstractFacade<Currency> {

    private static final String[] currenciesUsed = {"CZK"};
    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CurrencyFacade() {
        super(Currency.class);
    }

    public Currency findCzk() {
        return find("CZK");
    }

    public List<Currency> findAllCust() {
        List<Currency> findAll = findAll();
        List<Currency> currenciesList = new ArrayList<Currency>();
        currenciesList.add(find("USD"));
        for (Currency currency : findAll) {
            for (int i = 0; i < currenciesUsed.length; i++) {
                String string = currenciesUsed[i];
                if (currency.getCode().equalsIgnoreCase("USD")) {
                    continue;
                }
                if (currency.getCode().equalsIgnoreCase(string)) {
                    currenciesList.add(currency);
                }
            }
        }
        return currenciesList;
    }
}
