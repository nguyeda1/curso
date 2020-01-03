package com.schedek.curso.web.beans.task;

import com.schedek.curso.ejb.entities.Activity;
import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.Group;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.entities.Locality;
import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.TaskActivity;
import com.schedek.curso.ejb.entities.TaskComment;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.ejb.facade.LocalityFacade;
import com.schedek.curso.ejb.facade.SubtaskFacade;
import com.schedek.curso.ejb.facade.TaskCommentFacade;
import com.schedek.curso.ejb.facade.TaskFacade;
import com.schedek.curso.ejb.facade.GroupFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.web.beans.list.JPADataModel;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.context.RequestContext;
import util.FacesUtil;

@Named(value = "taskEditBean")
@ViewScoped
public class TaskEditBean implements Serializable {

    @Inject
    private SessionBean sb;
    @EJB
    TaskFacade tf;
    @EJB
    ListingFacade lif;
    @EJB
    LocalityFacade lf;
    @EJB
    SubtaskFacade stf;
    @EJB
    TaskCommentFacade tcf;
    private Task c;
    private String taskID;
    private String returnTo;
    @EJB
    ActivityFacade af;
    @EJB
    CaseFacade cf;
    @EJB
    UserFacade uf;
    @EJB
    GroupFacade gf;
    List<Subtask> subtaskList = null;
    Subtask newSubtask = null;
    private Listing copyLis = null;
    Subtask editSubtask = null;
    LazyDataModel<TaskActivity> taskActivities = null;

    List<User> users = null;
    List<TaskComment> comments;
    TaskComment newComment = new TaskComment();
    private String listingID;
    private List<Group> groups;

    public TaskEditBean() {
    }

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            _init();
        }
    }

    public void _init() {
        if (taskID != null) {
            c = tf.find(Long.parseLong(taskID));
            if (c == null) {
                throw new UnsupportedOperationException();
            }
        }
        if (c == null) {
            c = new Task();
            if (listingID != null) {
                Listing l = lif.find(Long.parseLong(listingID));
                if (l != null) {
                    c.setListing(l);
                }
            }
        }
        initGroups();
    }

    public void initGroups() {
        groups = gf.findAll();
        Group toRemove = null;
        for (Group group : groups) {
            if (group.getName().equals("operator")) {
                toRemove = group;
            }
        }
        if (toRemove != null) {
            groups.remove(toRemove);
        }
    }

    public List<Group> getGroups() {
        return groups;

    }


    public String getListingID() {
        return listingID;
    }

    public void setListingID(String listingID) {
        this.listingID = listingID;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getReturnTo() {
        return returnTo;
    }

    public void setReturnTo(String returnTo) {
        this.returnTo = returnTo;
    }

    public List<Case> getCursoCases() {
        return cf.findAllOrderByName();
    }

    public List<Listing> getListings() {
        return lif.findAll();
    }

    public List<Locality> getLocalities() {
        return lf.findAll();
    }

    public void reset() {

        newSubtask = null;
    }

    public Subtask getNewSubtask() {
        if (newSubtask == null) {
            newSubtask = new Subtask();
            newSubtask.setTask(c);
        }
        return newSubtask;
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

    public String copyTask() {
        Listing l = copyLis;
        Task copyTask = tf.copy(c, copyLis, sb.getUser());
        copyTask.setName(c.getName());
        copyTask.setDescription(c.getDescription());
        tf.edit(copyTask);
        tf.flush();

        return "edit.xhtml?faces-redirect=true&id=" + copyTask.getId();
    }

    public void setNewSubtask(Subtask newSubtask) {
        this.newSubtask = newSubtask;
    }

    public List<Subtask> getSubtaskList() {
        subtaskList = stf.findByTask(c);
        return subtaskList;
    }

    public void addNewSubTask() {
        stf.edit(newSubtask);
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public List<User> getUsers() {
        if (users == null) {
            users = uf.qbFilterAll(null).loadAll();
        }
        return users;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public Task getC() {
        return c;
    }

    public String finish() {
        if (!c.getFinishedState().isDone() && (c.getProblemText() == null || c.getProblemText().isEmpty())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Reason for not successfully completing the task is mandatory"));
        }
        c.setFinished(true);
        save();
        if ("CASE".equals(returnTo)) {
            return "/app/case/view.xhtml?faces-redirect=true&id=" + c.getCursoCase().getId();
        }
        String rs = "&taskState=";
        try {
            rs += (returnTo != null ? URLEncoder.encode(returnTo, "UTF-8") : "IN_PROGRESS");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TaskEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/app/task/index.xhtml?faces-redirect=true" + rs;
    }

    public void sendEmail() {
        if (c.isPurchaseItemCheck()) {
            if (c.getPurchasedItem() == null || c.getPurchasedItem().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Invalid item text"));
                return;
            }
            if (c.getEmailText() == null || c.getEmailText().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Invalid item text"));
                return;
            }
            tf.edit(c);
        }

    }

    public String justSave() {
        save();
        return "/app/task/edit.xhtml?faces-redirect=true&id=" + c.getId();

    }

    public String save() {

        if (Boolean.TRUE.equals(c.isFinished()) && c.getFinishedOn() == null) {
            c.setFinishedOn(new Date());
            c.setFinshedBy(sb.getUser());
        }
        if (!Boolean.TRUE.equals(c.isFinished()) && c.getFinishedOn() != null) {
            c.setFinishedOn(null);
            c.setFinshedBy(null);
        }
        if (c.getCursoCase() != null && c.getCursoCase().isTemplate() && c.getPlannedOnInDays() != null && c.getDeadlineInDays() != null && c.getPlannedOnInDays() > c.getDeadlineInDays()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Deadline has to be greater than Planned on"));
            return "";
        }
        if (c.getId() == null) {
            c.setCreatedBy(sb.getUser());
            tf.create(c);
        } else {
            tf.edit(c);
        }
        String rs = "&taskState=";
        try {
            rs += (returnTo != null ? URLEncoder.encode(returnTo, "UTF-8") : "IN_PROGRESS");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TaskEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ("CASE".equals(returnTo)) {
            return "/app/case/view.xhtml?faces-redirect=true&id=" + c.getCursoCase().getId();
        }
        return "/app/task/index.xhtml?faces-redirect=true" + rs;
    }

    public List<TaskComment> getComments() {
        if (comments == null) {
            comments = tcf.findByTask(c);
        }
        return comments;
    }

    public TaskComment getNewComment() {
        return newComment;
    }

    public void resetNewComment() {
        newComment = new TaskComment();
    }

    public void addNewComment() {
        newComment.setTask(c);
        newComment.setCreatedBy(sb.getUser());
        newComment.setCreatedOn(new Date());
        tcf.create(newComment);
        resetNewComment();
        comments = null;
    }

    public LazyDataModel<TaskActivity> getTaskActivity() {
        if (taskActivities == null) {
            taskActivities = new JPADataModel(af) {
                @Override
                protected QueryBuilder getQueryBuilder() {
                    return af.qbTaskActivityByTask(c);
                }
            ;
        }
        ;
                }
		return taskActivities;

    }

    public void finishStateSelectListener() {
        if (c.getFinishedState() == null || c.getFinishedState().isDone()) {
            c.setProblemText(null);
        }
    }

    public Task getForcedDelete() {
        return null;
    }

    public String setForcedDelete(Task delete) {
        try {
            tf.remove(delete);
        } catch (Exception e) {
            FacesUtil.processDbException("Delete failed", e);
        }
        return "/app/task/index.xhtml?faces-redirect=true";
    }

    public void changeCase() {
        if (c.getCursoCase() != null) {
            c.setListing(c.getCursoCase().getListing());
        }
    }

}
