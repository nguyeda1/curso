/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Locality;
import com.schedek.curso.ejb.facade.util.QueryBuilder;

import java.util.List;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dan Nguyen
 */
@Stateless
public class LocalityFacade extends AbstractFacade<Locality> {

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    public LocalityFacade() {
        super(Locality.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<SelectItem> getLocalitiesOptions() {
        return getFilterOptions("name");
    }

    public QueryBuilder<Locality> qbLocalities() {
        return new QueryBuilder<>(getEntityManager(), getEntityClass());
    }
}
