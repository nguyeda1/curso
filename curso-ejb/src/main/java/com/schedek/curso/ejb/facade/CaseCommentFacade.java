/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseActivity;
import com.schedek.curso.ejb.entities.CaseComment;
import com.schedek.curso.ejb.entities.TaskComment;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.ActivityType;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Dan Nguyen
 */
@Stateless
public class CaseCommentFacade extends AbstractFacade<CaseComment> {

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;
    @Resource
    private SessionContext ctx;
    @EJB
    ActivityFacade af;
    @EJB
    UserFacade uf;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CaseCommentFacade() {
        super(CaseComment.class);
    }

    @Override
    public void create(CaseComment entity) {
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

    public void create(CaseComment c, User u) {
        super.create(c);
        flush();
        CaseActivity a = buildLog(c);
        a.setCursoCase(c.getCursoCase());
        af.createLog(a, u);
    }

    private CaseActivity buildLog(CaseComment c) {
        CaseActivity a = new CaseActivity();
           a.setType(ActivityType.CASE_COMMENT);
        StringBuilder builder = new StringBuilder();
        builder.append("<strong>")
                .append(c.getCreatedBy().getFullname())
                .append("</strong> commented on case <strong>")
                .append(c.getCursoCase().getLabel())
                .append(":</strong>\n")
                .append(c.getText());
        a.setLog(builder.toString());
        return a;
    }

    public QueryBuilder<CaseComment> qbCaseCommentByCase(final Case c) {
        return new QueryBuilder<CaseComment>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<TaskComment> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("cursoCase"), c));
            }

            @Override
            protected Order[] getOrder(String column, Boolean asc) {
                return new Order[]{cb.desc(getRoot(entityClass).get("createdOn"))};
            }

        };
    }

    public List<CaseComment> findByCaseAndPage(Case c, int pageNumber, int pageSize) {
        return qbCaseCommentByCase(c).load((pageNumber) * pageSize, pageSize, "createdOn", Boolean.FALSE, null);
    }

    public List<CaseComment> findByCase(Case c) {
        return qbCaseCommentByCase(c).loadAll();
    }

    public int getCaseCommentsCountByCase(Case c) {
        return qbCaseCommentByCase(c).count(null);
    }

}
