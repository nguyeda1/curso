package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.entities.TaskActivity;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.ActivityChange;
import com.schedek.curso.ejb.enums.ActivityType;
import com.schedek.curso.ejb.enums.SubtaskState;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.python.google.common.base.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Dan Nguyen
 */
@Stateless
public class SubtaskFacade extends AbstractFacade<Subtask> {

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

    public SubtaskFacade() {
        super(Subtask.class);
    }

    @Override
    public void create(Subtask entity) {
        create(entity, getUserFromContext());
    }

    @Override
    public void edit(Subtask entity) {
        edit(entity, getUserFromContext());
    }

    public void create(Subtask updated, User u) {
        super.create(updated);
        flush();
        TaskActivity a = buildLog(new Subtask(), updated);
        a.setTask(updated.getTask());
        af.createLog(a, u);
    }

    public void edit(Subtask updated, User u) {
        if (updated.getId() == null) {
            create(updated, u);
        } else {
            Subtask old = find(updated.getId());
            if (old != null) {
                TaskActivity a = buildLog(old, updated);
                a.setTask(updated.getTask());
                af.createLog(a, u);
                super.edit(updated);
            }
        }
    }

    private TaskActivity buildLog(Subtask old, Subtask updated) {
        TaskActivity a = new TaskActivity();
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        
        builder.append("Subtask <strong>")
                .append(updated.getLabel())
                .append("</strong> on task ")
                .append("<strong>")
                .append(updated.getTask().getLabel())
                .append("</strong> was");
        
        if (old.getId() == null) {
            a.setType(ActivityType.TASK_CREATE);
            builder.append("created:");
        } else {
            a.setType(ActivityType.TASK_EDIT);
            builder.append("edited:");
        }
        builder.append("\n");

        if (!Objects.equal(old.getName(), updated.getName())) {
            a.getChanges().add(ActivityChange.SUBTASK_NAME);
            builder.append("-Name: <strong>")
                    .append(updated.getName())
                    .append("</strong>");
            builder.append("\n");
        }

        if (old.getState() != updated.getState()) {
            a.getChanges().add(ActivityChange.SUBTASK_STATE);
            builder.append("-State: <strong>");
            builder.append(updated.getState());
            builder.append("</strong>");
            builder.append("\n");
        }
        
        a.setLog(builder.toString());
        return a;
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

    public Long getSubtaskListCountByTask(Task c) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Subtask> criteriaQuery = criteriaBuilder
                .createQuery(Subtask.class);

        Root<Subtask> t = criteriaQuery.from(Subtask.class);

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

    public boolean isAllBegunByTask(Task c) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Subtask> criteriaQuery = criteriaBuilder
                .createQuery(Subtask.class);

        Root<Subtask> t = criteriaQuery.from(Subtask.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);

        countQuery.select(criteriaBuilder
                .count(t));

        if (c != null) {
            countQuery.where(criteriaBuilder.equal(t.get("task"), c), criteriaBuilder.equal(t.get("state"), SubtaskState.UNBEGUN));
        }
        Long count = em.createQuery(countQuery)
                .getSingleResult();

        return count == 0;
    }

    public QueryBuilder<Subtask> qbSubtaskByTask(final Task task) {
        return new QueryBuilder<Subtask>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Subtask> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("task"), task));
            }
        };
    }

    public List<Subtask> findByTask(Task task) {
        return qbSubtaskByTask(task).loadAll();
    }

}
