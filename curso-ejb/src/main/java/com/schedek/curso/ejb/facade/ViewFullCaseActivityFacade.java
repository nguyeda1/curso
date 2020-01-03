package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.ejb.view.ViewFullCaseActivity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
public class ViewFullCaseActivityFacade extends AbstractFacade<ViewFullCaseActivity> {

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ViewFullCaseActivityFacade() {
        super(ViewFullCaseActivity.class);
    }

    public QueryBuilder<ViewFullCaseActivity> qbFullCaseActivityByCursoCase(final Case c) {
        return new QueryBuilder<ViewFullCaseActivity>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<ViewFullCaseActivity> r = getRoot(ViewFullCaseActivity.class);
                return cb.equal(r.get("cursoCase"), c);
            }

            @Override
            protected Order[] getDefaultOrder() {
                return new Order[]{cb.desc(getRoot(ViewFullCaseActivity.class).get("id"))};
            }
        };
    }

}
