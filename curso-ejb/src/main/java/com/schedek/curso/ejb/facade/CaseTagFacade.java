/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.facade.util.QueryBuilder;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

/**
 * @author Tomáš
 */
@Stateless
public class CaseTagFacade extends AbstractFacade<CaseTag> {

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CaseTagFacade() {
        super(CaseTag.class);
    }

    public QueryBuilder<CaseTag> qbCaseTagByCase(final Case c) {
        return new QueryBuilder<CaseTag>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<CaseTag> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("cursoCase"), c));
            }
        };
    }

    public QueryBuilder<CaseTag> qbActive(final Boolean active) {
        return new QueryBuilder<CaseTag>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<CaseTag> rcvp = getRoot(getEntityClass());
                Predicate p = super.getPredicate();
                if (active) {
                    p = cb.and(p, cb.equal(rcvp.get("disabled"), false));
                }
                return p;
            }

            @Override
            protected Order[] getDefaultOrder() {
                Root<CaseTag> rcvp = getRoot(entityClass);
                return new Order[]{cb.asc(rcvp.get("id"))};
            }
        };
    }
    
    public QueryBuilder<CaseTag> qbActiveSortedbyName(final Boolean active) {
        return new QueryBuilder<CaseTag>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<CaseTag> rcvp = getRoot(getEntityClass());
                Predicate p = super.getPredicate();
                if (active) {
                    p = cb.and(p, cb.equal(rcvp.get("disabled"), false));
                }
                return p;
            }

            @Override
            protected Order[] getDefaultOrder() {
                Root<CaseTag> rcvp = getRoot(entityClass);
                return new Order[]{cb.asc(rcvp.get("name"))};
            }
        };
    }

    public List<CaseTag> findByCase(Case c) {
        return qbCaseTagByCase(c).loadAll();
    }
}
