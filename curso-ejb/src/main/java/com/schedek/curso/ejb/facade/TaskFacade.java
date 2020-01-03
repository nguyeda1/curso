/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.AppEJB;
import com.schedek.curso.ejb.entities.*;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseActivity;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.TaskActivity;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.ActivityChange;
import com.schedek.curso.ejb.enums.ActivityType;
import com.schedek.curso.ejb.enums.CaseOption;
import com.schedek.curso.ejb.enums.TaskFinishState;
import com.schedek.curso.ejb.enums.TaskPriority;
import com.schedek.curso.ejb.enums.TaskState;
import com.schedek.curso.ejb.facade.util.AdvancedFilter;
import com.schedek.curso.ejb.facade.util.MatchMode;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.ejb.util.CalendarData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.jpa.JpaQuery;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.python.google.common.base.Objects;

/**
 *
 * @author Tomáš
 */
@Stateless
public class TaskFacade extends AbstractFacade<Task> {

    @EJB
    ActivityFacade af;
    @EJB
    UserFacade uf;
    @EJB
    CaseFacade cf;
    @EJB
    SubtaskFacade stf;
    @EJB
    AppEJB app;
    @Resource
    private SessionContext ctx;

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TaskFacade() {
        super(Task.class);
    }

    @Override
    public void create(Task entity) {
        create(entity, getUserFromContext());
    }

    @Override
    public void edit(Task entity) {
        edit(entity, getUserFromContext());
    }

    @Override
    public void remove(Task entity) {
        remove(entity, getUserFromContext());
    }

    public void create(Task updated, User u) {
        super.create(updated);
        flush();
        TaskActivity a = buildLog(new Task(), updated);
        af.createLog(a, u);
    }

    public void edit(Task updated, User u) {
        if (updated.getId() == null) {
            create(updated, u);
        } else {
            Task old = find(updated.getId());
            if (old != null) {
                setPrevious(old, updated);
                TaskActivity a = buildLog(old, updated);
                af.createLog(a, u);
                super.edit(updated);
            }
        }
    }

    @Transactional
    public void remove(Task updated, User u) {
        Task old = find(updated.getId());
        if (old != null) {
            for (Activity a : af.findByTask(old)) {
                if (a instanceof TaskActivity) {
                    TaskActivity ta = (TaskActivity) a;
                    ta.setTask(null);
                    af.edit(ta);
                }
            }
            CaseActivity a = buildLog(old);
            a.setCursoCase(old.getCursoCase());
            af.createLog(a, u);
            super.remove(old);
        }
    }

    private void setPrevious(Task old, Task updated) {
        if (!Objects.equal(old.getAssignee(), updated.getAssignee())) {
            updated.setPreviousAssignee(old.getAssignee());
        }
        if (!Objects.equal(old.getCursoCase(), updated.getCursoCase())) {
            updated.setPreviousCase(old.getCursoCase());
        }
    }

    private CaseActivity buildLog(Task remove) {
        CaseActivity a = new CaseActivity();
        a.setType(ActivityType.TASK_REMOVE);
        StringBuilder builder = new StringBuilder();
        builder.append("Task <strong>#")
                .append(remove.getId())
                .append(" ")
                .append(remove.getName())
                .append("</strong> was removed");
        if (remove.getCursoCase() != null) {
            builder.append(" from case <strong>")
                    .append(remove.getCursoCase().getName())
                    .append("</strong>");
        }

        a.setLog(builder.toString());
        return a;
    }

    private TaskActivity buildLog(Task old, Task updated) {
        TaskActivity a = new TaskActivity();
        a.setTask(updated);

        StringBuilder builder = new StringBuilder();
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");

        builder.append("Task <strong>")
                .append(updated.getLabel())
                .append("</strong> was ");
        if (old.getId() == null) {
            a.setType(ActivityType.TASK_CREATE);
            builder.append("created:");
        } else {
            a.setType(ActivityType.TASK_EDIT);
            builder.append("edited:");
        }
        builder.append("\n");
        if (!Objects.equal(old.getName(), updated.getName())) {
            a.getChanges().add(ActivityChange.TASK_NAME);
            builder.append("-Name: <strong>")
                    .append(updated.getName())
                    .append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getAssignee(), updated.getAssignee())) {
            a.getChanges().add(ActivityChange.TASK_ASSIGNEE);
            if (updated.getPreviousAssignee() != null && updated.getAssignee() != null) {
                builder.append("-Reassigned from <strong>")
                        .append(updated.getPreviousAssignee().getFullname())
                        .append("</strong>")
                        .append(" to <strong>")
                        .append(updated.getAssignee().getFullname());
            } else if (updated.getAssignee() != null) {
                builder.append("-Assigned to: <strong>")
                        .append(updated.getAssignee().getFullname());
            } else {
                builder.append("-Unassigned from: <strong>")
                        .append(old.getAssignee().getFullname());
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getCursoCase(), updated.getCursoCase())) {
            a.getChanges().add(ActivityChange.TASK_CASE);
            builder.append("-Case: <strong>");
            if (updated.getCursoCase() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(updated.getCursoCase().getName());
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getListing(), updated.getListing())) {
            a.getChanges().add(ActivityChange.TASK_LISTING);
            builder.append("-Listing: <strong>");
            if (updated.getListing() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(updated.getListing().getName());
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getLocality(), updated.getLocality())) {
            a.getChanges().add(ActivityChange.TASK_LOCALITY);
            builder.append("-Locality: <strong>");
            if (updated.getLocality() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(updated.getLocality().getName());
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (old.getFinishedState() != updated.getFinishedState()) {
            a.getChanges().add(ActivityChange.TASK_STATE);
            builder.append("-Finished state: <strong>");
            if (updated.isFinished() == true) {
                builder.append(updated.getFinishedState().getLabel());
            } else {
                builder.append("In progress");
            }
            builder.append("</strong>");
            builder.append("\n");
            if (updated.getFinishedState() != TaskFinishState.DONE) {
                builder.append("-Problem: <strong>")
                        .append(updated.getProblemText())
                        .append("</strong>")
                        .append("\n");
            }
        }

        if (!Objects.equal(old.getPlannedOn(), updated.getPlannedOn())) {
            a.getChanges().add(ActivityChange.TASK_PLANNED_ON);
            builder.append("-Planned on: <strong>");
            if (updated.getPlannedOn() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(formater.format(updated.getPlannedOn()));
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getDeadline(), updated.getDeadline())) {
            a.getChanges().add(ActivityChange.TASK_DEADLINE);
            builder.append("-Deadline: <strong>");
            if (updated.getDeadline() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(formater.format(updated.getDeadline()));
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getPriority(), updated.getPriority())) {
            a.getChanges().add(ActivityChange.TASK_PRIORITY);
            builder.append("-Priority: <strong>");
            if (updated.getPriority() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(updated.getPriority());
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getApprovedWithClient(), updated.getApprovedWithClient())) {
            a.getChanges().add(ActivityChange.TASK_CLIENT_APPROVED);
            builder.append("-Approved with client: <strong>");
            if (updated.getApprovedWithClient() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(updated.getApprovedWithClient().getLabel());
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getGuestInformed(), updated.getGuestInformed())) {
            a.getChanges().add(ActivityChange.TASK_GUEST_INFORMED);
            builder.append("-Guest was informed: <strong>");
            if (updated.getGuestInformed() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(updated.getGuestInformed().getLabel());
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getDescription(), updated.getDescription())) {
            a.getChanges().add(ActivityChange.TASK_DESCRIPTION);
            builder.append("-Description: <strong>")
                    .append(StringUtils.abbreviate(updated.getDescription(), 13))
                    .append("</strong>");
            builder.append("\n");
        }
        a.setLog(builder.toString());
        return a;
    }

    public Task copy(Task c, Listing l, User userCreatedBy) {
        Task newTask = new Task();
        newTask.setListing(l);
        newTask.setCreatedBy(userCreatedBy);
        newTask.setCreatedOn(new Date());
        newTask.setDescription(c.getDescription());
        newTask.setFinishedOn(c.getFinishedOn());
        newTask.setName(c.getName());
        newTask.setPreviousAssignee(c.getPreviousAssignee());
        newTask.setPriority(c.getPriority());
        create(newTask);
        newTask.setCursoCase(c.getCursoCase());
        newTask = find(newTask.getId());
        List<Subtask> subtaskList = stf.findByTask(c);

        for (Subtask st : subtaskList) {
            Subtask pom = new Subtask();

            pom.setName(st.getName());
            pom.setTask(newTask);
            stf.create(pom);

        }
        flush();

        return newTask;
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

    public List<Task> findNotFinishedByCase(Case c) {
        if (c != null) {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Task> criteriaQuery = criteriaBuilder
                    .createQuery(Task.class);
            Root<Task> t = criteriaQuery.from(Task.class);

            criteriaQuery.orderBy(criteriaBuilder.desc(t.get("deadline")));

            CriteriaQuery<Task> select = criteriaQuery.select(t);

            select.where(criteriaBuilder.equal(t.get("cursoCase"), c), criteriaBuilder.equal(t.get("finished"), false));

            TypedQuery<Task> typedQuery = em.createQuery(select);

            return typedQuery.getResultList();
        }
        return null;
    }

    public int countAssignedByUser(Booking b) {
        return 0;
    }

    public List<Task> activeTasksForTodayByUser(User u) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> r = cq.from(Task.class);
        cq.where(cb.and(
                cb.equal(r.get("assignee"), u),
                cb.isNull(r.get("finishedState")),
                cb.equal(r.get("plannedOn"), CalendarData.getDateOnly(new Date())))
        );
        cq.orderBy(cb.asc(r.get("priorityOnScreen")));
        cq.select(r);
        return em.createQuery(cq).getResultList();
    }

    public int countByListingSuitable(Booking b) {
        if (b != null && b.getListing() != null) {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Task> criteriaQuery = criteriaBuilder
                    .createQuery(Task.class);
            Root<Task> t = criteriaQuery.from(Task.class);

            CriteriaQuery<Task> select = criteriaQuery.select(t);
            select.where(criteriaBuilder.and(criteriaBuilder.equal(t.get("suitableForSupplyCheckDepartment"), true), criteriaBuilder.equal(t.get("listing"), b.getListing()), criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo(t.<Date>get("deadline"), new Date()), criteriaBuilder.and(criteriaBuilder.isNull(t.get("deadline")), criteriaBuilder.greaterThanOrEqualTo(t.get("cursoCase").<Date>get("deadline"), new Date())))));
            TypedQuery<Task> typedQuery = em.createQuery(select);

            CriteriaQuery<Task> select2 = criteriaQuery.select(t);
            select2.where(criteriaBuilder.and(criteriaBuilder.equal(t.get("suitableForSupplyCheckDepartment"), true), criteriaBuilder.equal(t.get("listing"), b.getListing()), criteriaBuilder.greaterThanOrEqualTo(t.<Date>get("deadline"), new Date()), criteriaBuilder.isNull(t.get("cursoCase"))));
            TypedQuery<Task> typedQuery2 = em.createQuery(select2);

            int i = typedQuery.getResultList().size();
            int e = typedQuery2.getResultList().size();
            return i + e;
        }
        return 0;
    }

    public List<Task> getByListingSuitable(Booking b) {
        if (b != null && b.getListing() != null) {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Task> criteriaQuery = criteriaBuilder
                    .createQuery(Task.class);
            Root<Task> t = criteriaQuery.from(Task.class);

            CriteriaQuery<Task> select = criteriaQuery.select(t);
            select.where(criteriaBuilder.and(criteriaBuilder.equal(t.get("suitableForSupplyCheckDepartment"), true), criteriaBuilder.equal(t.get("listing"), b.getListing()), criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo(t.<Date>get("deadline"), new Date()), criteriaBuilder.and(criteriaBuilder.isNull(t.get("deadline")), criteriaBuilder.greaterThanOrEqualTo(t.get("cursoCase").<Date>get("deadline"), new Date())))));
            TypedQuery<Task> typedQuery = em.createQuery(select);

            CriteriaQuery<Task> select2 = criteriaQuery.select(t);
            select2.where(criteriaBuilder.and(criteriaBuilder.equal(t.get("suitableForSupplyCheckDepartment"), true), criteriaBuilder.equal(t.get("listing"), b.getListing()), criteriaBuilder.greaterThanOrEqualTo(t.<Date>get("deadline"), new Date()), criteriaBuilder.isNull(t.get("cursoCase"))));
            TypedQuery<Task> typedQuery2 = em.createQuery(select2);

            List<Task> i = new ArrayList<>();
            i.addAll(typedQuery.getResultList());
            i.addAll(typedQuery2.getResultList());

            return i;
        }
        return null;
    }

    public Long getTaskListCountByCriteria(Case c, User u) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Task> criteriaQuery = criteriaBuilder
                .createQuery(Task.class);

        Root<Task> t = criteriaQuery.from(Task.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);

        countQuery.select(criteriaBuilder
                .count(t));

        if (c != null) {
            countQuery.where(criteriaBuilder.equal(t.get("cursoCase"), c), criteriaBuilder.equal(t.get("finished"), false));
        }
        if (u != null) {
            countQuery.where(criteriaBuilder.equal(t.get("assignee"), u), criteriaBuilder.equal(t.get("finished"), false));
        }

        return em.createQuery(countQuery)
                .getSingleResult();
    }

    public void review(Task t, User user) {
        t.setReviewedBy(user);
        em.merge(t);
    }

    public QueryBuilder<Task> qbTaskByName(final String name) {
        return new QueryBuilder<Task>(getEntityManager(), getEntityClass()) {
            @Override
            protected Predicate getPredicate() {
                Root<Task> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("name"), name));
            }
        };
    }

    public QueryBuilder<Task> qbActiveTaskByPlannedOn(final Date date) {
        return new QueryBuilder<Task>(getEntityManager(), getEntityClass()) {
            @Override
            protected Predicate getPredicate() {
                Root<Task> r = getRoot(getEntityClass());
                return cb.and(cb.equal(r.get("plannedOn"), CalendarData.getDateOnly(date)),
                        cb.equal(r.get("finished"), false));
            }
        };
    }

    public List<Task> findByName(String name) {
        return qbTaskByName(name).loadAll();
    }

    public QueryBuilder<Task> qbTaskByAssignee(final User user, final boolean finished) {
        return new QueryBuilder<Task>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Task> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("assignee"), user),
                        cb.equal(rcvp.get("finished"), finished),
                        cb.isNotNull(rcvp.get("plannedOn")));
            }

            @Override
            protected Order[] getOrder(String column, Boolean asc) {
                return new Order[]{cb.asc(getRoot(getEntityClass()).get("plannedOn"))};
            }
        };
    }

    public List<Task> findNotFinishedByAssignee(User user) {
        return qbTaskByAssignee(user, false).load(0, 0, null, null, null);
    }

    public QueryBuilder<Task> qbTaskByFinishedBy(final User user, final boolean finished) {
        return new QueryBuilder<Task>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Task> rcvp = getRoot(getEntityClass());
                return cb.and(
                        cb.or(cb.equal(rcvp.get("finshedBy"), user),
                                cb.equal(rcvp.get("assignee"), user)),
                        cb.equal(rcvp.get("finished"), finished));
            }

            @Override
            protected Order[] getOrder(String column, Boolean asc) {
                return new Order[]{cb.desc(getRoot(entityClass).get("finishedOn"))};
            }
        };
    }

    public List<Task> findFinishedByAssignee(User user, int pageNumber, int pageSize) {
        return qbTaskByFinishedBy(user, true).load(pageNumber * pageSize, pageSize, null, null, null);
    }

    public int getFinishedByAssigneeCount(User user) {
        return qbTaskByFinishedBy(user, true).count(null);
    }

    public Task findMyFinishedTasksById(User u, final Long id) {
        Map<String, Object> filters = new HashMap<String, Object>() {
            {
                put("id", id);
            }
        };
        List<Task> tasks = qbTaskByFinishedBy(u, true).load(0, 0, null, null, filters);
        if (tasks.size() == 1) {
            return tasks.get(0);
        }
        return null;
    }

    public List<Task> asyncSearchMyFinishedTasks(User u, final String query) {
        final AdvancedFilter advFilter = new AdvancedFilter(MatchMode.STARTS_WITH_MATCH_MODE, query);
        Map<String, Object> filters = new HashMap<String, Object>() {
            {
                put("name", advFilter);
            }
        };
        return qbTaskByFinishedBy(u, true).load(0, 0, "name", Boolean.TRUE, filters);
    }

    public QueryBuilder<Task> qbTaskByCursoCase(final Case cursoCase) {
        return new QueryBuilder<Task>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Task> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("cursoCase"), cursoCase));
            }
        };
    }

    public List<Task> findByCursoCase(Case cursoCase) {
        return qbTaskByCursoCase(cursoCase).loadAll();
    }

    public List<Task> findByCursoCaseOrdered(Case cursoCase) {
        return qbTaskByCursoCaseOrdered(cursoCase).loadAll();
    }

    public QueryBuilder<Task> qbTaskByCursoCaseOrdered(final Case cursoCase) {
        return new QueryBuilder<Task>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Task> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("cursoCase"), cursoCase));
            }

            @Override
            protected Order[] getDefaultOrder() {
                return new Order[]{cb.desc(getRoot(getEntityClass()).get("priorityOnScreen"))};
            }
        };
    }

    public QueryBuilder<Task> qbTaskByFinishedAndProblem(final Boolean finished,
            final Boolean problem,
            final User assignee,
            final Group role,
            final Date plannedOnFilter,
            final Date deadlineFilter,
            final Date finishedOnFilter) {
        return new QueryBuilder<Task>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Task> rcvp = getRoot(getEntityClass());
                Predicate p = super.getPredicate();
                Predicate q = cb.disjunction();

                Join<Task, Case> jn = rcvp.join("cursoCase", JoinType.LEFT);
                Predicate y = cb.isNull(jn.get("template"));
                Predicate z = cb.equal(jn.get("template"), false);
                Predicate xx = cb.or(y, z);
                p = cb.and(xx, p);

                if (plannedOnFilter != null) {
                    p = cb.and(cb.equal(rcvp.get("plannedOn"), CalendarData.getDateOnly(plannedOnFilter)), p);
                }
                if (deadlineFilter != null) {
                    p = cb.and(cb.equal(rcvp.get("deadline"), CalendarData.getDateOnly(deadlineFilter)), p);
                }
                if (finishedOnFilter != null) {
                    p = cb.and(cb.between(rcvp.<Date>get("finishedOn"), CalendarData.getDateOnly(finishedOnFilter), CalendarData.getNextDate(finishedOnFilter)), p);
                }
                if (role != null) {
                    q = cb.or(cb.equal(rcvp.get("assigneeRole"), role), q);
                }
                if (assignee != null) {
                    q = cb.or(cb.equal(rcvp.get("assignee"), assignee), q);
                }
                if (role != null || assignee != null) {
                    p = cb.and(p, q);
                }
                if (problem) {
                    return cb.and(cb.or(cb.equal(rcvp.get("finishedState"), TaskFinishState.PARTIALLY), cb.equal(rcvp.get("finishedState"), TaskFinishState.NOT_DONE), cb.equal(rcvp.get("finishedState"), TaskFinishState.NO_TIME)), cb.isNull(rcvp.get("reviewedBy")), p);
                }
                if (finished) {
                    return cb.and(cb.equal(rcvp.get("finished"), finished), cb.or(cb.equal(rcvp.get("finishedState"), TaskFinishState.DONE), cb.isNotNull(rcvp.get("reviewedBy"))), p);
                } else {
                    return cb.and(cb.equal(rcvp.get("finished"), finished), p);
                }
            }

            @Override
            protected Order[] getDefaultOrder() {
                return new Order[]{cb.desc(getRoot(getEntityClass()).get("finishedOn")), cb.desc(cb.isNull(getRoot(getEntityClass()).get("deadline"))),
                    cb.asc(getRoot(getEntityClass()).get("deadline"))};
            }
        };
    }

    public QueryBuilder<Task> qbTaskByFinishedAndListing(final Boolean finished, final Listing listing) {
        return new QueryBuilder<Task>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Task> rcvp = getRoot(getEntityClass());
                Join<Task, Case> join = rcvp.join("cursoCase", JoinType.LEFT);
                Predicate x, y;
                if (finished == null) {
                    x = cb.equal(rcvp.get("listing"), listing);
                    y = cb.equal(join.get("listing"), listing);
                } else {
                    x = cb.and(cb.equal(rcvp.get("finished"), finished), cb.equal(rcvp.get("listing"), listing));
                    y = cb.and(cb.equal(rcvp.get("finished"), finished), cb.equal(join.get("listing"), listing));
                }
                return cb.or(x, y);
            }

            @Override
            protected Order[] getDefaultOrder() {
                return new Order[]{cb.desc(getRoot(getEntityClass()).get("finishedOn")), cb.desc(cb.isNull(getRoot(getEntityClass()).get("deadline"))),
                    cb.asc(getRoot(getEntityClass()).get("deadline"))};
            }
        };
    }

    public QueryBuilder<Task> qbTaskByDateFromAndListingAndPlannedOrDeadline(final Date from, final Listing listing, final Date bookingStart, final Date bookingEnd) {
        return new QueryBuilder<Task>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Task> rcvp = getRoot(getEntityClass());
                Join<Task, Case> join = rcvp.join("cursoCase", JoinType.LEFT);
                Predicate x = cb.and(cb.greaterThanOrEqualTo(rcvp.<Date>get("plannedOn"), CalendarData.getDateOnly(from)), cb.equal(rcvp.get("listing"), listing));
                Predicate y = cb.and(cb.equal(join.get("listing"), listing), cb.greaterThanOrEqualTo(rcvp.<Date>get("plannedOn"), CalendarData.getDateOnly(from)));
                Predicate fin = cb.or(x, y);
                if (bookingStart != null && bookingEnd != null) {
                    Predicate z = cb.or(cb.and(cb.between(rcvp.<Date>get("plannedOn"), bookingStart, bookingEnd)), cb.and(cb.between(rcvp.<Date>get("deadline"), bookingStart, bookingEnd)));
                    fin = cb.and(fin, z);
                }
                return fin;
            }
        };
    }

    public QueryBuilder<Task> qbTaskByDateFromAndListing(final Date from, final Listing listing) {
        return qbTaskByDateFromAndListingAndPlannedOrDeadline(from, listing, null, null);
    }

    public QueryBuilder<Task> findTaskByLockboxBooking(final Booking booking) {
        return new QueryBuilder<Task>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Task> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("lockboxBooking"), booking));
            }
        };
    }

    private String convertTimeToString(int time) {
        return time < 10 ? "0" + String.valueOf(time) : String.valueOf(time);
    }
}
