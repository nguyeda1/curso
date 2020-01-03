/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.CaseFeedback;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.TaskActivity;
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
public class TaskCommentFacade extends AbstractFacade<TaskComment> {

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

    public TaskCommentFacade() {
        super(TaskComment.class);
    }

    @Override
    public void create(TaskComment entity) {
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

    public void create(TaskComment c, User u) {
        super.create(c);
        flush();
        TaskActivity a = buildLog(c);
        a.setTask(c.getTask());
        af.createLog(a, u);
    }

    private TaskActivity buildLog(TaskComment c) {
        TaskActivity a = new TaskActivity();
        a.setType(ActivityType.TASK_COMMENT);
        StringBuilder builder = new StringBuilder();
        builder.append("<strong>")
                .append(c.getCreatedBy().getFullname())
                .append("</strong> commented on case <strong>")
                .append(c.getTask().getLabel())
                .append(":</strong>\n")
                .append(c.getText());
        a.setLog(builder.toString());
        return a;
    }

    public List<TaskComment> findByTaskAndPage(Task c, int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<TaskComment> criteriaQuery = criteriaBuilder
                .createQuery(TaskComment.class);
        Root<TaskComment> t = criteriaQuery.from(TaskComment.class);

        criteriaQuery.orderBy(criteriaBuilder.desc(t.get("createdOn")));

        CriteriaQuery<TaskComment> select = criteriaQuery.select(t);

        if (c != null) {
            select.where(criteriaBuilder.equal(t.get("task"), c));
        }

        TypedQuery<TaskComment> typedQuery = em.createQuery(select);

        typedQuery.setFirstResult((pageNumber) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    public Long getTaskCommentListCountByTask(Task c) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<TaskComment> criteriaQuery = criteriaBuilder
                .createQuery(TaskComment.class);

        Root<TaskComment> t = criteriaQuery.from(TaskComment.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);

        countQuery.select(criteriaBuilder
                .count(t));

        if (c != null) {
            countQuery.where(criteriaBuilder.equal(t.get("task"), c));
        }

        return em.createQuery(countQuery)
                .getSingleResult();
    }

    public QueryBuilder<TaskComment> qbTaskCommentByTask(final Task task) {
        return new QueryBuilder<TaskComment>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<TaskComment> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("task"), task));
            }
        };
    }

    public List<TaskComment> findByTask(Task task) {
        return qbTaskCommentByTask(task).loadAll();
    }

}
