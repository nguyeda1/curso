/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.AppEJB;
import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseActivity;
import com.schedek.curso.ejb.entities.Activity;
import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.entities.Contact;
import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.TaskActivity;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.ActivityChange;
import com.schedek.curso.ejb.enums.CaseOption;
import com.schedek.curso.ejb.enums.ContactType;
import com.schedek.curso.ejb.facade.util.AdvancedFilter;
import com.schedek.curso.ejb.facade.util.MatchMode;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.jpa.JpaQuery;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.eclipse.persistence.sessions.DatabaseSession;

/**
 *
 * @author Tomáš
 */
@Stateless
public class ActivityFacade extends AbstractFacade<Activity> {

    @EJB
    private ContactFacade cf;
    @EJB
    private AppEJB app;
    @EJB
    private TaskFacade tf;

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActivityFacade() {
        super(Activity.class);
    }

    public Activity createLog(Activity activity, User currentUser) {
        activity.setCreatedBy(currentUser);
        activity.setCreatedOn(new Date());
        create(activity);
        sendRmNotification(activity);

        return activity;
    }

    private void sendRmNotification(Activity activity) {
        List<User> users = new ArrayList<User>();

        if (activity instanceof CaseActivity) {
            CaseActivity caseActivity = (CaseActivity) activity;
            Case ccase = caseActivity.getCursoCase();
            if (ccase == null) {
                return;
            }
            if (caseToNotify(ccase, activity)) {
                sendRmNotificationInternal(activity);
            }
            users.addAll(ccase.getFollowers());
            if (!users.contains(ccase.getAssignee())) {
                users.add(ccase.getAssignee());
            }
            if (!users.contains(ccase.getOwner())) {
                users.add(ccase.getOwner());
            }
            if (!users.contains(ccase.getCreatedBy())) {
                users.add(ccase.getCreatedBy());
            }
            if (ccase.getRole() == null
                    || ccase.getRole().getName().equals("cleaning")
                    || ccase.getRole().getName().equals("operationsmanagement")
                    || ccase.getRole().getName().equals("supply_check")) {
                sendRmNotificationToCoordinationChannel(activity);
            }

        }
        if (activity instanceof TaskActivity) {
            TaskActivity taskActivity = (TaskActivity) activity;
            Task task = taskActivity.getTask();

            if (!users.contains(task.getAssignee())) {
                users.add(task.getAssignee());
            }
            if (task.getAssignee() != null && task.getAssignee().isInternalSales()) {
                sendRmNotificationInternal(activity);
            }
        }
        if (!users.contains(activity.getCreatedBy())) {
            users.add(activity.getCreatedBy());
        }
        for (User user : users) {
            if (user != null) {
                sendRmNotificationToUser(user, activity);
            }
        }

    }

    public boolean caseToNotify(Case ccase, Activity activity) {
        if (ccase.getRole() !=null &&(ccase.getRole().getName().equals("sales")
                || ccase.getRole().getName().equals("internalsales"))) {
            return true;
        } else if (activity.getCreatedBy().isInternalSales() || activity.getCreatedBy().isSales()) {
            return true;
        } else {
            for (Task task : tf.findByCursoCase(ccase)) {
                if (task.getAssignee() != null && (task.getAssignee().isInternalSales() || task.getAssignee().isSales())) {
                    return true;
                }
            }
        }
        return false;

    }

    public void sendRmNotificationInternal(Activity a) {
        String log = a.getLog();
        String link = "";
        if (a instanceof CaseActivity) {
            CaseActivity c = (CaseActivity) a;
            link += app.getConfig().getInternalUrl() + "/app/case/view.xhtml?id=" + c.getCursoCase().getId();
        } else if (a instanceof TaskActivity) {
            TaskActivity c = (TaskActivity) a;
            link += app.getConfig().getInternalUrl() + "/app/task/view.xhtml?id=" + c.getTask().getId();
        } 
    }

    public void sendRmNotificationToUser(User u, Activity a) {
        String log = a.getLog();
        String link = "";
        if (a instanceof CaseActivity) {
            CaseActivity c = (CaseActivity) a;
            link += app.getConfig().getTaskmanagerUrl() + "/#/case/view/" + c.getCursoCase().getId();
        } else if (a instanceof TaskActivity) {
            TaskActivity c = (TaskActivity) a;
            link += app.getConfig().getTaskmanagerUrl() + "/#/task/view/" + c.getTask().getId();
        } 
    }

    public void sendRmNotificationToCoordinationChannel(Activity a) {
        String log = a.getLog();
        String link = "";
        if (a instanceof CaseActivity) {
            CaseActivity c = (CaseActivity) a;
            link += app.getConfig().getInternalUrl() + "/app/case/view.xhtml?id=" + c.getCursoCase().getId();
        } else if (a instanceof TaskActivity) {
            TaskActivity t = (TaskActivity) a;
            link += app.getConfig().getInternalUrl() + "/app/task/view.xhtml?id=" + t.getTask().getId();
        }
    }

    public User tagOperator() {
        User u = null;
        List<Contact> contactList = getContactList();
        for (Contact contact : contactList) {
            if (contact.getType().isOperation()) {
                u = contact.getUser();
            }
        }
        return u;
    }

    private List<Contact> getContactList() {

        ArrayList<Contact> contactList;
        contactList = new ArrayList();
        contactList.addAll(cf.findAll());
        List<ContactType> typeList = new ArrayList();
        if (!contactList.isEmpty()) {
            for (Contact c : contactList) {
                typeList.add(c.getType());
            }
        }
        for (ContactType ct : ContactType.values()) {
            if (!typeList.contains(ct)) {
                Contact newC = new Contact();
                newC.setType(ct);
                cf.create(newC);
                contactList.add(newC);
            }
        }
        return contactList;
    }

    public QueryBuilder<Activity> qbActivityByInterestedParties(final User u) {
        return new QueryBuilder<Activity>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Activity> a = getRoot(Activity.class);

                Subquery<Long> userSq = cq.subquery(Long.class);
                Root<Activity> sa = userSq.from(Activity.class);
                Root<CaseActivity> sca = cb.treat(sa, CaseActivity.class);
                Join<CaseActivity, Case> caseJoin = sca.join("cursoCase");
                Join<Case, User> followers = caseJoin.join("followers");
                userSq.select(sca.get("id").as(Long.class));
                if (u != null) {
                    userSq.where(cb.equal(followers.get("id"), u.getId()));
                }

                Subquery<Long> caseSq = cq.subquery(Long.class);
                Root<Activity> xa = caseSq.from(Activity.class);
                Root<CaseActivity> xca = cb.treat(xa, CaseActivity.class);
                caseSq.select(xca.get("id").as(Long.class));
                if (u != null) {
                    caseSq.where(cb.or(
                            cb.equal(xca.get("cursoCase").get("owner"), u),
                            cb.equal(xca.get("cursoCase").get("assignee"), u),
                            cb.equal(xca.get("cursoCase").get("createdBy"), u),
                            cb.equal(xca.get("createdBy"), u)
                    ));
                }
                Subquery<Long> taskSq = cq.subquery(Long.class);
                Root<Activity> ya = taskSq.from(Activity.class);
                Root<TaskActivity> yca = cb.treat(ya, TaskActivity.class);
                taskSq.select(yca.get("id").as(Long.class));
                if (u != null) {
                    taskSq.where(
                            cb.or(
                                    cb.equal(yca.get("createdBy"), u),
                                    cb.equal(yca.get("task").get("assignee"), u)
                            //                                cb.equal(yca.get("task").get("cursoCase").get("owner"), u), cb.equal(yca.get("task").get("cursoCase").get("assignee"), u),
                            //                                cb.equal(yca.get("task").get("cursoCase").get("createdBy"), u),
                            //                                cb.and(cb.equal(yca.get("task").get("previousAssignee"), u),
                            //                                        cb.in(yca.get("changes")).value(ActivityChange.TASK_ASSIGNEE))
                            )
                    );
                }

                return cb.or(
                        cb.in(a.get("id")).value(userSq),
                        cb.in(a.get("id")).value(caseSq),
                        cb.in(a.get("id")).value(taskSq)
                );
            }

            @Override
            protected Order[] getDefaultOrder() {
                Root<Activity> a = getRoot(Activity.class);
                return new Order[]{cb.desc(a.get("createdOn"))};
            }
        };
    }

    public QueryBuilder<Activity> qbActivityByCase(final Case c) {
        return new QueryBuilder<Activity>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Activity> a = getRoot(Activity.class);

                Subquery<Long> caseSq = cq.subquery(Long.class);
                Root<Activity> xa = caseSq.from(Activity.class);
                Root<CaseActivity> xca = cb.treat(xa, CaseActivity.class);
                caseSq.select(xca.get("id").as(Long.class));
                caseSq.where(
                        cb.equal(xca.get("cursoCase"), c)
                );

                Subquery<Long> taskSq = cq.subquery(Long.class);
                Root<Activity> ya = taskSq.from(Activity.class);
                Root<TaskActivity> yca = cb.treat(ya, TaskActivity.class);
                taskSq.select(yca.get("id").as(Long.class));
                taskSq.where(
                        cb.equal(yca.get("task").get("cursoCase"), c)
                );

                return cb.or(
                        cb.in(a.get("id")).value(taskSq),
                        cb.in(a.get("id")).value(caseSq));
            }

            @Override
            protected Order[] getDefaultOrder() {
                Root<Activity> a = getRoot(Activity.class);
                Root<CaseActivity> ca = cb.treat(a, CaseActivity.class);

                return new Order[]{cb.desc(ca.get("createdOn"))};
            }
        };
    }

    public List<Activity> findNew(Date lastFetch, User u) {
        final AdvancedFilter advFilter = new AdvancedFilter(MatchMode.GREATER_THAN_EQUALS_MODE, lastFetch);
        Map<String, Object> filters = new HashMap<String, Object>() {
            {
                put("createdOn", advFilter);
            }
        };
        return qbActivityByInterestedParties(u).load(0, 0, "createdOn", Boolean.FALSE, filters);
    }

    public int getUnreadActivityCount(User u, Date lastFetch) {
        final AdvancedFilter advFilter = new AdvancedFilter(MatchMode.GREATER_THAN_EQUALS_MODE, lastFetch);
        Map<String, Object> filters = new HashMap<String, Object>() {
            {
                put("createdOn", advFilter);
            }
        };
        return qbActivityByInterestedParties(u).count(filters);
    }

    public List<Activity> findByInterestedParties(User u, int pageNumber, int pageSize) {
        return qbActivityByInterestedParties(u).load(pageNumber * pageSize, pageSize, "createdOn", Boolean.FALSE, null);
    }

    public int getActivityCountByInterestedParties(User u) {
        return qbActivityByInterestedParties(u).count(null);
    }

    public List<Activity> findByCaseAndPage(Case c, int pageNumber, int pageSize) {
        return qbActivityByCase(c).load(pageNumber * pageSize, pageSize, "createdOn", Boolean.FALSE, null);
    }

    public int getActivityCountByCase(Case c) {
        return qbActivityByCase(c).count(null);
    }

    public QueryBuilder<Activity> qbCaseActivityByCursoCase(final Case cursoCase) {
        return new QueryBuilder<Activity>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Activity> a = getRoot(Activity.class);
                Root<CaseActivity> ca = cb.treat(a, CaseActivity.class);
                return cb.and(cb.equal(ca.get("cursoCase"), cursoCase));
            }

            @Override
            protected Order[] getDefaultOrder() {
                Root<Activity> a = getRoot(Activity.class);
                Root<CaseActivity> ca = cb.treat(a, CaseActivity.class);

                return new Order[]{cb.desc(ca.get("createdOn"))};
            }
        };
    }

    public List<Activity> findByCursoCase(Case cursoCase) {
        return qbCaseActivityByCursoCase(cursoCase).loadAll();
    }

    public QueryBuilder<TaskActivity> qbTaskActivityByTask(final Task task) {
        return new QueryBuilder<TaskActivity>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Activity> a = getRoot(Activity.class);
                Root<TaskActivity> ta = cb.treat(a, TaskActivity.class);
                return cb.and(cb.equal(ta.get("task"), task));
            }

            @Override
            protected Order[] getDefaultOrder() {
                Root<Activity> a = getRoot(Activity.class);
                Root<TaskActivity> ta = cb.treat(a, TaskActivity.class);

                return new Order[]{cb.desc(ta.get("createdOn"))};
            }
        };
    }


    public List<TaskActivity> findByTask(Task task) {
        return qbTaskActivityByTask(task).loadAll();
    }

}
