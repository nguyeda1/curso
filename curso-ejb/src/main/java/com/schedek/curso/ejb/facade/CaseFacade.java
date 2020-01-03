/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseActivity;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.entities.Group;
import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.TaskComment;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.ActivityChange;
import com.schedek.curso.ejb.enums.ActivityType;
import com.schedek.curso.ejb.enums.CaseRole;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.python.google.common.base.Objects;

/**
 *
 * @author Tomáš
 */
@Stateless
public class CaseFacade extends AbstractFacade<Case> {

    @EJB
    ActivityFacade af;
    @EJB
    UserFacade uf;
    @EJB
    TaskFacade tf;
    @Resource
    private SessionContext ctx;
    @EJB
    TaskCommentFacade tcf;
    @EJB
    SubtaskFacade stf;

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CaseFacade() {
        super(Case.class);
    }

    @Override
    public void create(Case updated) {
        create(updated, getUserFromContext());
    }

    public void create(Case updated, User u) {
        super.create(updated);
        flush();
        CaseActivity a = buildLog(new Case(), updated);
        a.setCursoCase(updated);
        af.createLog(a, u);
    }

    @Override
    public void edit(Case updated) {
        edit(updated, getUserFromContext());
    }

    public void edit(Case updated, User u) {
        if (updated.getId() == null) {
            create(updated, u);
        } else {
            Case old = find(updated.getId());
            if (old != null) {
                setPrevious(old, updated);
                CaseActivity a = buildLog(old, updated);
                a.setCursoCase(updated);
                af.createLog(a, u);
                super.edit(updated);
            }
        }
    }

    public void addNoteLog(Case updated, User u, String text) {
        if (updated.getId() != null) {
            Case old = find(updated.getId());
            if (old != null) {
                StringBuilder builder = new StringBuilder();
                CaseActivity a = new CaseActivity();
                a.setCursoCase(updated);
                a.setType(ActivityType.CASE_NOTE);
                builder.append("<strong>")
                        .append(u.getFullname())
                        .append("</strong> added a customer note on <strong>")
                        .append(updated.getLabel())
                        .append(":</strong>\n")
                        .append(text);
                a.setLog(builder.toString());
                af.createLog(a, u);
            }
        }
    }

    public Case copy(Case c, Listing l, User u, boolean copyTask) {
        Case newCase = new Case();
        newCase.setBooking(c.getBooking());
        newCase.setCaseState(CaseState.NEW);
        newCase.setCreatedBy(u);
        newCase.setCreatedOn(new Date());
        newCase.setDeadline(c.getDeadline());
        if (c.isTemplate()) {
            newCase.setAssignee(c.getAssignee());
        }
        newCase.setDescription(c.getDescription());
        newCase.setFinishedBy(c.getFinishedBy());
        newCase.setFinishedOn(c.getFinishedOn());
        newCase.setName(c.getName());
        newCase.setTemplate(false);
        if (c.getOwner() != null && c.isTemplate()) {
            newCase.setOwner(c.getOwner());
        } else {
            newCase.setOwner(u);
        }
        newCase.setPreviousAssignee(c.getPreviousAssignee());
        newCase.setPreviousOwner(c.getPreviousOwner());
        newCase.setPriority(c.getPriority());
        newCase.setTags(c.getTags());
        newCase.setRole(c.getRole());
        newCase.setFollowers(c.getFollowers());
        create(newCase);

        newCase = find(newCase.getId());
        if (copyTask) {
            List<Task> tasks = tf.findByCursoCase(c);

            for (Task t : tasks) {
                Task pom = new Task();
                pom.setCursoCase(newCase);
                pom.setCustomLocality(t.getCustomLocality());
                pom.setDescription(t.getDescription());
                pom.setEmailText(t.getEmailText());
                pom.setEstimatedPurchasePrice(t.getEstimatedPurchasePrice());
                pom.setFinished(t.isFinished());
                pom.setFinshedBy(t.getFinshedBy());
                pom.setListing(l);
                pom.setLocality(t.getLocality());
                pom.setLockboxBooking(t.getLockboxBooking());
                pom.setName(t.getName());
                pom.setPlannedOn(t.getPlannedOn());
                pom.setPreviousCase(t.getPreviousCase());
                pom.setPriority(t.getPriority());
                pom.setPriorityOnScreen(t.getPriorityOnScreen());
                pom.setPurchasedItem(t.getPurchasedItem());
                pom.setReviewedBy(t.getReviewedBy());
                if (c.isTemplate()) {
                    pom.setAssignee(t.getAssignee());
                    pom.setDeadlineInDays(t.getDeadlineInDays());
                    pom.setPlannedOnInDays(t.getPlannedOnInDays());
                }
                tf.create(pom);
                pom = tf.find(pom.getId());

                pom.setSubtask(new ArrayList());
                pom.setComments(new ArrayList());

                for (Subtask s : t.getSubtask()) {
                    Subtask newS = new Subtask();
                    newS.setName(s.getName());
                    newS.setTask(pom);
                    pom.getSubtask().add(s);
                    stf.create(newS);
                }
                for (TaskComment tc : t.getComments()) {
                    TaskComment newC = new TaskComment();
                    newC.setTask(pom);
                    newC.setText(tc.getText());
                    pom.getComments().add(newC);
                    tcf.create(newC);
                }

                tf.edit(pom);
            }
        }
        flush();

        return newCase;
    }

    public void followLog(Case updated, User u) {
        if (updated.getId() != null) {
            Case old = find(updated.getId());
            if (old != null) {
                StringBuilder builder = new StringBuilder();
                CaseActivity a = new CaseActivity();
                a.setCursoCase(updated);
                a.setType(ActivityType.CASE_FOLLOW);
                builder.append(updated.getFollowers().contains(u)
                        ? "</strong> started following case <strong>"
                        : "</strong> unfollowed case <strong>")
                        .append(updated.getLabel())
                        .append("</strong>")
                        .append("\n");
                a.setLog(builder.toString());
                af.createLog(a, u);
            }
            super.edit(updated);
        }
    }

    public void uploadPhotoLog(Case updated, User u, Integer number) {
        if (updated.getId() != null) {
            Case old = find(updated.getId());
            if (old != null) {
                StringBuilder builder = new StringBuilder();
                CaseActivity a = new CaseActivity();
                a.setCursoCase(updated);
                a.setType(ActivityType.CASE_PHOTO);
                builder.append("<strong>")
                        .append(u.getFullname())
                        .append("</strong> uploaded ")
                        .append(number)
                        .append(" ")
                        .append(number > 1 ? "photos" : "photo")
                        .append(" on case <strong>")
                        .append(updated.getLabel())
                        .append("</strong>")
                        .append("\n");
                a.setLog(builder.toString());
                af.createLog(a, u);
            }
        }
    }

    private void setPrevious(Case old, Case updated) {
        if (!Objects.equal(old.getAssignee(), updated.getAssignee())) {
            updated.setPreviousAssignee(old.getAssignee());
        }
        if (!Objects.equal(old.getOwner(), updated.getOwner())) {
            updated.setPreviousOwner(old.getOwner());
        }
    }

    private CaseActivity buildLog(Case old, Case updated) {
        CaseActivity a = new CaseActivity();
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");

        builder.append("Case <strong>")
                .append(updated.getLabel())
                .append("</strong> was ");
        if (old.getId() == null) {
            a.setType(ActivityType.CASE_CREATE);
            builder.append("created:");
        } else {
            a.setType(ActivityType.CASE_EDIT);
            builder.append("edited:");
        }
        builder.append("\n");
        if (!Objects.equal(old.getName(), updated.getName())) {
            a.getChanges().add(ActivityChange.CASE_NAME);
            builder.append("-Name: <strong>")
                    .append(updated.getName())
                    .append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getCaseState(), updated.getCaseState())) {
            a.getChanges().add(ActivityChange.CASE_STATE);
            builder.append("-State: <strong>")
                    .append(updated.getCaseState().getLabel())
                    .append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getAssignee(), updated.getAssignee())) {
            a.getChanges().add(ActivityChange.CASE_ASSIGNEE);
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

        if (!Objects.equal(old.getOwner(), updated.getOwner())) {
            a.getChanges().add(ActivityChange.CASE_OWNER);
            if (updated.getPreviousOwner() != null && updated.getOwner() != null) {
                builder.append("-Owner changed from <strong>")
                        .append(updated.getPreviousOwner().getFullname())
                        .append("</strong>")
                        .append(" to <strong>")
                        .append(updated.getOwner().getFullname());
            } else if (updated.getOwner() != null) {
                builder.append("-Owner: <strong>")
                        .append(updated.getOwner().getFullname());
            } else {
                builder.append("-Owner: <strong>")
                        .append("REMOVED");
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getListing(), updated.getListing())) {
            a.getChanges().add(ActivityChange.CASE_LISTING);
            builder.append("-Listing: <strong>");
            if (updated.getListing() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(updated.getListing().getName());
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getBooking(), updated.getBooking())) {
            a.getChanges().add(ActivityChange.CASE_BOOKING);
            builder.append("-Booking: <strong>");
            if (updated.getBooking() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(updated.getBooking().getGuestName());
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getDeadline(), updated.getDeadline())) {
            a.getChanges().add(ActivityChange.CASE_DEADLINE);
            builder.append("-Deadline: <strong>");
            if (updated.getDeadline() == null) {
                builder.append("REMOVED");
            } else {
                builder.append(formater.format(updated.getDeadline()));
            }
            builder.append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getFinishedOn(), updated.getFinishedOn())) {
            a.getChanges().add(ActivityChange.CASE_FINISHED_ON);
            builder.append("-Finished on: <strong>")
                    .append(formater.format(updated.getFinishedOn()))
                    .append("</strong>");
            builder.append("\n");
        }

        if (!Objects.equal(old.getPriority(), updated.getPriority())) {
            a.getChanges().add(ActivityChange.CASE_PRIORITY);
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
            a.getChanges().add(ActivityChange.CASE_CLIENT_APPROVED);
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
            a.getChanges().add(ActivityChange.CASE_GUEST_INFORMED);
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
            a.getChanges().add(ActivityChange.CASE_DESCRIPTION);
            builder.append("-Description: <strong>")
                    .append(StringUtils.abbreviate(updated.getDescription(), 13))
                    .append("</strong>");
            builder.append("\n");
        }

        if (!CollectionUtils.isEqualCollection(old.getTags(), updated.getTags())) {
            a.getChanges().add(ActivityChange.CASE_TAGS);
            builder.append("-Tags: <strong>UPDATED</strong>");
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

    public List<Case> findByStateAndPage(CaseState state, int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Case> criteriaQuery = criteriaBuilder.createQuery(Case.class);
        Root<Case> c = criteriaQuery.from(Case.class);

        criteriaQuery.orderBy(criteriaBuilder.desc(c.get("createdOn")));

        CriteriaQuery<Case> select = criteriaQuery.select(c);

        if (state != null) {
            select.where(criteriaBuilder.equal(c.get("caseState"), state));
        }

        TypedQuery<Case> typedQuery = em.createQuery(select);

        typedQuery.setFirstResult(
                (pageNumber) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    public Long getCaseListCountByState(CaseState state) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Case> criteriaQuery = criteriaBuilder
                .createQuery(Case.class
                );

        Root<Case> c = criteriaQuery.from(Case.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        countQuery.select(criteriaBuilder.count(c));

        if (state != null) {
            countQuery.where(criteriaBuilder.equal(c.get("caseState"), state));
        }

        return em.createQuery(countQuery)
                .getSingleResult();
    }

    public List<Case> findMyUnfinishedCases(User u, int pageNumber, int pageSize) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Case> criteriaQuery = criteriaBuilder
                .createQuery(Case.class);
        Root<Case> c = criteriaQuery.from(Case.class);

        criteriaQuery.orderBy(criteriaBuilder.desc(c.get("createdOn")));
        CriteriaQuery<Case> select = criteriaQuery.select(c);

        select.where(getMyCasesPredicate(criteriaBuilder, criteriaQuery, c, u));

        TypedQuery<Case> typedQuery = em.createQuery(select);

        typedQuery.setFirstResult((pageNumber) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    public Long getMyUnfinishedCasesCount(User u) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Case> criteriaQuery = criteriaBuilder
                .createQuery(Case.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);

        Root<Case> c = criteriaQuery.from(Case.class);
        criteriaQuery.orderBy(criteriaBuilder.desc(c.get("createdOn")));

        countQuery.select(criteriaBuilder
                .count(c));

        countQuery.where(getMyCasesPredicate(criteriaBuilder, criteriaQuery, c, u));

        return em.createQuery(countQuery)
                .getSingleResult();
    }

    public Case findMyCasesById(User u, Long id) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Case> criteriaQuery = criteriaBuilder
                .createQuery(Case.class);
        Root<Case> c = criteriaQuery.from(Case.class);

        CriteriaQuery<Case> select = criteriaQuery.select(c);

        select.where(criteriaBuilder.and(
                criteriaBuilder.equal(c.get("id"), id),
                getMyCasesPredicate(criteriaBuilder, criteriaQuery, c, u)));
        TypedQuery<Case> typedQuery = em.createQuery(select);
        List<Case> res = typedQuery.getResultList();
        if (res.size() == 1) {
            return res.get(0);
        }
        return null;
    }

    public List<Case> asyncSearchMyCases(User u, String query) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Case> criteriaQuery = criteriaBuilder
                .createQuery(Case.class);
        Root<Case> c = criteriaQuery.from(Case.class);

        String q = (query + "%").toLowerCase();

        CriteriaQuery<Case> select = criteriaQuery.select(c);

        select.where(criteriaBuilder.and(
                criteriaBuilder.like(criteriaBuilder.lower(c.<String>get("name")), q),
                getMyCasesPredicate(criteriaBuilder, criteriaQuery, c, u)));

        TypedQuery<Case> typedQuery = em.createQuery(select);

        return typedQuery.getResultList();
    }

    private Predicate getMyCasesPredicate(CriteriaBuilder cb, CriteriaQuery cq, Root c, User u) {
        Subquery<User> userSq = cq.subquery(User.class);
        Root<Case> aSubroot = userSq.correlate(c);
        Join<Case, User> followers = aSubroot.join("followers");
        userSq.select(followers);
        userSq.where(cb.equal(followers.get("id"), u.getId()));
        Predicate roleOr = cb.and(cb.notEqual(c.get("caseState"), CaseState.DONE),
                cb.or(cb.exists(userSq),
                        cb.equal(c.get("createdBy"), u),
                        cb.equal(c.get("owner"), u),
                        cb.equal(c.get("assignee"), u)
                )
        );
        return roleOr;
    }

    public Case findByIdAndState(CaseState state, Long id) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Case> criteriaQuery = criteriaBuilder
                .createQuery(Case.class
                );
        Root<Case> c = criteriaQuery.from(Case.class
        );

        CriteriaQuery<Case> select = criteriaQuery.select(c);
        if (state != null) {
            select.where(criteriaBuilder.equal(c.get("id"), id), criteriaBuilder.equal(c.get("caseState"), state));
        } else {
            select.where(criteriaBuilder.equal(c.get("id"), id));
        }

        TypedQuery<Case> typedQuery = em.createQuery(select);
        List<Case> res = typedQuery.getResultList();
        if (res.size() == 1) {
            return res.get(0);
        }
        return null;
    }

    public List<Case> asyncSearch(CaseState state, String query) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Case> criteriaQuery = criteriaBuilder
                .createQuery(Case.class
                );
        Root<Case> c = criteriaQuery.from(Case.class
        );

        String q = (query + "%").toLowerCase();
        Predicate or = criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(c.<String>get("name")), q)
        );

        CriteriaQuery<Case> select = criteriaQuery.select(c);
        if (state != null) {
            select.where(or, criteriaBuilder.equal(c.get("caseState"), state));
        } else {
            select.where(or);
        }

        TypedQuery<Case> typedQuery = em.createQuery(select);

        return typedQuery.getResultList();
    }

    public QueryBuilder<Case> qbCaseByCaseState(final CaseState caseState) {
        return qbCaseByCaseState(caseState, null, null);
    }

    public QueryBuilder<Case> qbCaseByCaseState(final CaseState caseState, final User assignee, final List<CaseTag> tags) {
        return new QueryBuilder<Case>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Case> rcvp = getRoot(getEntityClass());
                Predicate p = super.getPredicate();
                if (caseState != null) {
                    p = cb.and(cb.equal(rcvp.get("caseState"), caseState), p);
                }
                if (assignee != null) {
                    p = cb.and(cb.equal(rcvp.get("assignee"), assignee), p);
                }
                if (tags != null && !tags.isEmpty()) {
                    Predicate z = cb.disjunction();
                    for (CaseTag tag : tags) {
                        Predicate w = cb.isMember(tag, rcvp.<List<CaseTag>>get("tags"));
                        z = cb.or(z, w);
                    }
                    p = cb.and(p, z);
                }
                return p;
            }

            @Override
            protected Path getDefaultSortColumnPath() {
                return getRoot(entityClass).get("deadline");
            }
        };

    }

    public QueryBuilder<Case> qbCaseByCaseStateAndListingAndDepartment(final CaseState caseState, final User assignee, final Listing listing, final String department) {
        return new QueryBuilder<Case>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Case> rcvp = getRoot(getEntityClass());
                Predicate p = super.getPredicate();
                if (caseState != null) {
                    p = cb.and(cb.equal(rcvp.get("caseState"), caseState), p);
                }
                if (assignee != null) {
                    p = cb.and(cb.equal(rcvp.get("assignee"), assignee), p);
                }
                if (listing != null) {
                    p = cb.and(cb.equal(rcvp.get("listing"), listing), p);
                }
                if (department != null) {
                    Join<Case, Group> role = rcvp.join("role");
                    p = cb.and(cb.equal(role.get("name"), department), p);
                }
                return p;
            }
        };
    }

    public QueryBuilder<Case> qbCaseByCaseStateAndListing(final CaseState caseState, final User assignee, final Listing listing) {
        return qbCaseByCaseStateAndListingAndDepartment(caseState, assignee, listing, null);
    }

    public QueryBuilder<Case> qbCaseByName(final String name) {
        return new QueryBuilder<Case>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Case> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("name"), name));
            }
        };
    }

    public List<Case> findByName(String name) {
        return qbCaseByName(name).loadAll();
    }

    public QueryBuilder<Case> qbCaseByCreatedOn(final Date createdOn) {
        return new QueryBuilder<Case>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Case> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("createdOn"), createdOn));
            }
        };
    }

    public QueryBuilder<Case> qbTemplates(final Boolean template) {
        return new QueryBuilder<Case>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Case> rcvp = getRoot(getEntityClass());
                return cb.and(cb.equal(rcvp.get("template"), template));
            }
        };
    }

    public List<Case> findByCreatedOn(Date createdOn) {
        return qbCaseByCreatedOn(createdOn).loadAll();
    }

    public List<Case> findByCaseState(CaseState caseState) {
        return qbCaseByCaseState(caseState).loadAll();
    }

    public QueryBuilder<Case> qbCaseByStateAssigneeTagsOwnerMyListings(final CaseState caseState, final User assignee,
            final List<CaseTag> tags, final User owner,
            final User myListings,
            final CaseRole caseRole,
            final List<Group> groups,
            final Group role
    ) {
        return new QueryBuilder<Case>(getEntityManager(), getEntityClass()) {

            @Override
            protected Predicate getPredicate() {
                Root<Case> rcvp = getRoot(getEntityClass());
                Predicate p = super.getPredicate();
                p = cb.and(cb.or(cb.equal(rcvp.get("template"), false), cb.isNull(rcvp.get("template"))), p);
                if (caseState != null) {
                    p = cb.and(cb.equal(rcvp.get("caseState"), caseState), p);
                }
                if (assignee != null) {
                    p = cb.and(cb.equal(rcvp.get("assignee"), assignee), p);
                }
                if (owner != null) {
                    p = cb.and(cb.equal(rcvp.get("owner"), owner), p);
                }
                if (role != null) {
                    p = cb.and(cb.equal(rcvp.get("role"), role), p);
                }
                if (caseRole != null) {
                    if (caseRole.isSales()) {
                        p = cb.and(cb.equal(rcvp.get("owner").get("groups").get("name"), "sales"), p);
                    }
                    if (caseRole.isCommunication()) {
                        p = cb.and(cb.equal(rcvp.get("owner").get("groups").get("name"), "operator"), p);
                    }
                    if (caseRole.isOther()) {
                        p = cb.and(cb.notEqual(rcvp.get("owner").get("groups").get("name"), "sales"), cb.notEqual(rcvp.get("owner").get("groups").get("name"), "operator"), p);
                    }

                }
                if (myListings != null) {
                    p = cb.and(cb.equal(rcvp.get("listing").get("customer").get("salesRepre1"), myListings), p);
                }
                if (tags != null && !tags.isEmpty()) {
                    Predicate z = cb.disjunction();
                    for (CaseTag tag : tags) {
                        Predicate w = cb.isMember(tag, rcvp.<List<CaseTag>>get("tags"));
                        z = cb.or(z, w);
                    }
                    p = cb.and(p, z);
                }
                if (groups != null && !groups.isEmpty()) {
                    Predicate z = cb.disjunction();
                    for (Group group : groups) {
                        z = cb.or(cb.equal(rcvp.get("role"), group), cb.isNull(rcvp.get("role")), z);
                    }
                    z = cb.or(cb.isNull(rcvp.get("role")), z);
                    p = cb.and(p, z);
                }
                return p;
            }

            @Override
            protected Path getDefaultSortColumnPath() {
                return getRoot(entityClass).get("deadline");
            }
        };
    }

    public List<Case> findAllOrderByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Case> cq = cb.createQuery(Case.class);
        Root<Case> r = cq.from(Case.class);
        cq.orderBy(cb.asc(r.get("name")));
        cq.select(r);
        return em.createQuery(cq).getResultList();
    }
}
