package com.schedek.curso.ejb.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Razzier
 */
@Stateless
public class ViewActivityFacade extends AbstractFacade<ViewActivityFacade> {

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ViewActivityFacade() {
        super(ViewActivityFacade.class);
    }
}
