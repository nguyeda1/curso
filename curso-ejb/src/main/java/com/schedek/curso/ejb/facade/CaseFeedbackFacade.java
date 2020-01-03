/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseActivity;
import com.schedek.curso.ejb.entities.CaseFeedback;
import com.schedek.curso.ejb.entities.TaskComment;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.ActivityType;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Dan Nguyen
 */
@Stateless
public class CaseFeedbackFacade extends AbstractFacade<CaseFeedback> {

    @EJB
    ActivityFacade af;
    @EJB
    UserFacade uf;
    @Resource
    private SessionContext ctx;

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CaseFeedbackFacade() {
        super(CaseFeedback.class);
    }

    @Override
    public void create(CaseFeedback entity) {
        create(entity, getUserFromContext());
    }

    private User getUserFromContext() {
        User u = null;
        if (ctx != null && ctx.getCallerPrincipal() != null) {
            String p = ctx.getCallerPrincipal().getName();
            if (p != null) {
                u = uf.findOneByUsername(p);

            }
        }
        if (u == null) {
            throw new EJBException("Error getting User from context");
        }
        return u;
    }

    public void create(CaseFeedback c, User u) throws EJBException {
        super.create(c);
        flush();
        CaseActivity a = buildLog(c);
        a.setCursoCase(c.getCursoCase());
        af.createLog(a, u);
    }

    private CaseActivity buildLog(CaseFeedback c) {
        CaseActivity a = new CaseActivity();
        a.setType(ActivityType.CASE_FEEDBACK);
        StringBuilder builder = new StringBuilder();
        builder.append("<strong>")
                .append(c.getReviewedBy().getFullname())
                .append("</strong> created a feedback on case <strong>#")
                .append(c.getCursoCase().getLabel());
        a.setLog(builder.toString());
        return a;
    }

    public List<CaseFeedback> findByCaseAndPage(Case c, int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<CaseFeedback> criteriaQuery = criteriaBuilder
                .createQuery(CaseFeedback.class);
        Root<CaseFeedback> f = criteriaQuery.from(CaseFeedback.class);

        criteriaQuery.orderBy(criteriaBuilder.desc(f.get("reviewedOn")));

        CriteriaQuery<CaseFeedback> select = criteriaQuery.select(f);

        if (c != null) {
            select.where(criteriaBuilder.equal(f.get("cursoCase"), c));
        }

        TypedQuery<CaseFeedback> typedQuery = em.createQuery(select);

        typedQuery.setFirstResult((pageNumber) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    public Long getFeedbackListCountByCase(Case c) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<CaseFeedback> criteriaQuery = criteriaBuilder
                .createQuery(CaseFeedback.class);

        Root<CaseFeedback> f = criteriaQuery.from(CaseFeedback.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);

        countQuery.select(criteriaBuilder
                .count(f));

        if (c != null) {
            countQuery.where(criteriaBuilder.equal(f.get("cursoCase"), c));
        }

        return em.createQuery(countQuery)
                .getSingleResult();
    }

    public QueryBuilder<CaseFeedback> qbCaseByCase(final Case c) {
        return new QueryBuilder<CaseFeedback>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<CaseFeedback> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("cursoCase"), c));
            }
        };
    }

    public List<CaseFeedback> findByCase(Case c) {
        return qbCaseByCase(c).loadAll();
    }
}
