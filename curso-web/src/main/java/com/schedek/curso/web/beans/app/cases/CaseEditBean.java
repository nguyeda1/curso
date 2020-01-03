package com.schedek.curso.web.beans.app.cases;

import com.schedek.curso.ejb.entities.*;
import com.schedek.curso.ejb.enums.CaseState;

import static com.schedek.curso.ejb.enums.CaseState.NEW;

import com.schedek.curso.ejb.facade.*;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.ejb.util.CalendarData;
import com.schedek.curso.ejb.view.ViewFullCaseActivity;
import com.schedek.curso.web.beans.list.JPADataModel;
import com.schedek.curso.web.beans.sess.SessionBean;
import com.schedek.curso.web.beans.task.TaskEditBean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.primefaces.model.LazyDataModel;
import org.python.icu.util.Calendar;

@Named(value = "caseEditBean")
@ViewScoped
public class CaseEditBean implements Serializable {

    @Inject
    private SessionBean sb;
    @EJB
    CaseFacade cf;
    private Case c;
    private String caseID;
    private String returnTo;
    @EJB
    BookingFacade bf;
    @EJB
    ListingFacade lf;
    @EJB
    TaskFacade tf;
    @EJB
    UserFacade uf;
    @EJB
    CaseTagFacade ctf;
    @EJB
    GroupFacade gf;
    @EJB
    ActivityFacade af;
    @EJB
    ViewFullCaseActivityFacade vfcaf;
    @EJB
    CaseCommentFacade ccf;
    @EJB
    CaseFeedbackFacade eff;
    LazyDataModel<ViewFullCaseActivity> caseActivities;

    private Listing copyLis = null;
    private List<Booking> bookings = null;
    private List<User> users = null;
    private List<Listing> listings = null;
    private List<Task> tasks = null;
    private List<CaseTag> tags = null;
    private Task newTask = null;
    private CaseFeedback guestFeedback = new CaseFeedback();
    private CaseFeedback hostFeedback = new CaseFeedback();
    private List<CaseFeedback> feedbacks;
    private User newFollower;
    private String listingID;
    private String template;
    private Group tempGroup;

    List<CaseComment> comments;
    CaseComment newComment = new CaseComment();

    public CaseEditBean() {
    }

    public User getNewFollower() {
        return newFollower;
    }

    public void setNewFollower(User newFollower) {
        this.newFollower = newFollower;
    }

    public String getListingID() {
        return listingID;
    }

    public void setListingID(String listingID) {
        this.listingID = listingID;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Group getTempGroup() {
        return tempGroup;
    }

    public void setTempGroup(Group tempGroup) {
        this.tempGroup = tempGroup;
    }

    public void listenerFollowerChosen(AjaxBehaviorEvent abe) {
        if (!c.getFollowers().contains(newFollower)) {
            c.getFollowers().add(newFollower);
        }
        cf.edit(c);
        newFollower = null;

    }

    public CaseFeedback getGuestFeedback() {
        return guestFeedback;
    }

    public void setGuestFeedback(CaseFeedback guestFeedback) {
        this.guestFeedback = guestFeedback;
    }

    public CaseFeedback getHostFeedback() {
        return hostFeedback;
    }

    public void setHostFeedback(CaseFeedback hostFeedback) {
        this.hostFeedback = hostFeedback;
    }

    public List<CaseFeedback> getFeedbacks() {
        return eff.findByCase(c);
    }

    public void setFeedbacks(List<CaseFeedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public void resetFeedback() {
        guestFeedback = new CaseFeedback();
        hostFeedback = new CaseFeedback();
    }


    public void addFeedback() {

        try {
            guestFeedback.setCursoCase(c);
            hostFeedback.setCursoCase(c);

            if (!(guestFeedback.getType() == null)) {
                if (!(guestFeedback.getType().isNone())) {
                    guestFeedback.setReviewedBy(sb.getUser());
                    guestFeedback.setReviewedOn(new Date());
                    guestFeedback.setGuestInformed(true);
                    if (guestFeedback.getType().isNone()) {
                        guestFeedback.setDescription("");
                    }
                } else {
                    guestFeedback = null;
                }
            } else {
                guestFeedback = null;
            }

            if (!(hostFeedback.getType() == null)) {
                if (!(hostFeedback.getType().isNone())) {
                    hostFeedback.setReviewedBy(sb.getUser());
                    hostFeedback.setReviewedOn(new Date());
                    hostFeedback.setHostInformed(true);
                    if (hostFeedback.getType().isNone()) {
                        hostFeedback.setDescription("");
                    }
                } else {
                    hostFeedback = null;
                }
            } else {
                hostFeedback = null;
            }

            if (guestFeedback != null) {
                eff.create(guestFeedback);
            }
            if (hostFeedback != null) {
                eff.create(hostFeedback);
            }

            guestFeedback = new CaseFeedback();
            hostFeedback = new CaseFeedback();
        } catch (EJBException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Description is too long"));
        }
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addBookingsList() {
        if (c.getBooking() != null) {
            List<Booking> b = new ArrayList();
            boolean flag = false;
//            b.addAll(bf.findByListingLite(c.getListing()));
            for (Booking bb : b) {
                if (Objects.equals(bb.getId(), c.getBooking().getId())) {
                    flag = true;
                }
            }
            if (!flag) {
                b.add(c.getBooking());
            }
            bookings = b;
        } else {
//            bookings = bf.findByListingLite(c.getListing());
        }
    }

    public List<CaseTag> getTags() {
        if (tags == null) {
            tags = ctf.qbActiveSortedbyName(true).loadAll();
        }
        return tags;
    }

    public void setTags(List<CaseTag> tags) {
        this.tags = tags;
    }

    public List<Listing> getListings() {
        if (listings == null) {
            listings = lf.findActive();
        }
        return listings;
    }

    public List<User> getUsers() {
        if (users == null) {
            users = uf.qbFilterAll(null).loadAll();
        }
        return users;
    }

    public List<Group> getGroups() {
        return gf.findByDepartment(true);
    }

    public void stateChange(final AjaxBehaviorEvent event) {
        boolean isDone = c.getCaseState().equals(CaseState.DONE);
        c.setFinishedOn(isDone ? new Date() : null);
        c.setFinishedBy(isDone ? sb.getUser() : null);
    }

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (caseID != null) {
                c = cf.find(Long.parseLong(caseID));
                if (c == null) {
                    throw new UnsupportedOperationException();
                }
                addBookingsList();
            }
            if (c == null) {
                c = new Case();
                c.setCreatedBy(sb.getUser());
                c.setOwner(sb.getUser());
                c.setCaseState(NEW);
                c.setFinishedOn(null);

                if (listingID != null) {
                    Listing l = lf.find(Long.parseLong(listingID));
                    if (l != null) {
                        c.setListing(l);
                    }
                }
            }
            tempGroup = c.getRole();
            if (c.getRole() == null) {
                tempGroup = gf.findByName("operationsmanagement");

            }
        }
    }

    public void reset() {
        newTask = null;

    }

    public void resetCopy() {
        copyLis = null;
    }

    public Listing getCopyLis() {
        return copyLis;
    }

    public void setCopyLis(Listing copyLis) {
        this.copyLis = copyLis;
    }

    public String copyCase() {
        Listing l = copyLis;
        Case copyCase = cf.copy(c, l, sb.getUser(), true);
        copyCase.setListing(l);
        cf.edit(copyCase);
        cf.flush();
        for (Task t : tf.findByCursoCase(copyCase)) {
            t.setPlannedOn(t.getPlannedOnInDays() == null ? null : CalendarData.getDateAddWorkingDays(t.getCreatedOn(), t.getPlannedOnInDays().intValue()));
            t.setDeadline(t.getDeadlineInDays() == null ? null : CalendarData.getDateAddWorkingDays(t.getCreatedOn(), t.getDeadlineInDays().intValue()));
            tf.edit(t);
        }

        return "edit.xhtml?faces-redirect=true&id=" + copyCase.getId();
    }

    public void addNewTask() {

        if (newTask.getName().length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Name is not selected", null));
            return;
        }
//		if (newTask.getAssignee() == null) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Assignee is not selected", null));
//			return;
//		}
        newTask.setCreatedBy(sb.getUser());
        tf.create(newTask);
    }


    public Task getNewTask() {
        if (newTask == null) {
            newTask = new Task();
            newTask.setCursoCase(c);
            newTask.setCreatedOn(new Date());
        }
        return newTask;
    }

    public void setNewTask(Task newTask) {
        this.newTask = newTask;
    }

    public List<Task> getTasks() {
        tasks = tf.findByCursoCaseOrdered(c);
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public String getReturnTo() {
        return returnTo;
    }

    public void setReturnTo(String returnTo) {
        this.returnTo = returnTo;
    }

    public Case getC() {
        return c;
    }

    public String finish() {
        save();
        String rs = "&caseState=";
        try {
            rs += (returnTo != null ? URLEncoder.encode(returnTo, "UTF-8") : "null");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TaskEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (template != null && template.equals("true")) {
            rs += "&template=true";
        }
        if (c.getRole() != null) {
            rs += "&role=" + c.getRole().getName();
        }
        return "index.xhtml?faces-redirect=true" + rs;
    }

    public String save() {
        if (tempGroup != null) {
            c.setRole(tempGroup);
        }
        if (c.getId() == null) {
            cf.create(c);
        } else {
            cf.edit(c);
        }
        String rs = "";
        try {
            rs = (returnTo != null ? "&return=" + URLEncoder.encode(returnTo, "UTF-8") : "");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TaskEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (template != null && template.equals("true")) {
            rs += "&template=true";
        }

        return "edit.xhtml?faces-redirect=true&id=" + c.getId() + rs;
    }

    public List<CaseComment> getComments() {
        if (comments == null) {
            comments = ccf.findByCase(c);
        }
        return comments;
    }

    public CaseComment getNewComment() {
        return newComment;
    }

    public void resetNewComment() {
        newComment = new CaseComment();
    }

    public void addNewComment() {
        newComment.setCursoCase(c);
        newComment.setCreatedBy(sb.getUser());
        newComment.setCreatedOn(new Date());
        ccf.create(newComment);
        resetNewComment();
        comments = null;
    }

    public LazyDataModel<ViewFullCaseActivity> getCaseActivity() {
        if (caseActivities == null) {
            caseActivities = new JPADataModel<ViewFullCaseActivity>(vfcaf) {
                @Override
                protected QueryBuilder getQueryBuilder() {
                    return vfcaf.qbFullCaseActivityByCursoCase(c);
                }
            };
        }
        return caseActivities;
    }

    public void addFollower() {

        c = cf.find(c.getId());
        List<User> f = c.getFollowers();
        for (User u : f) {
            if (u.equals(sb.getUser())) {
                return;
            }
        }
        f.add(sb.getUser());
        c.setFollowers(f);
        cf.edit(c);

    }

    public void removeFollower() {

        c = cf.find(c.getId());
        List<User> f = c.getFollowers();
        List<User> newf = new ArrayList();
        for (User u : f) {
            if (!u.equals(sb.getUser())) {
                newf.add(u);
            }
        }
        f.add(sb.getUser());
        c.setFollowers(newf);
        cf.edit(c);
    }

    public boolean isFollower() {
        List<User> f = c.getFollowers();
        User us = sb.getUser();
        if (f == null) {
            return false;
        }
        for (User u : f) {
            if (u.equals(us)) {
                return true;
            }
        }
        return false;
    }
}
