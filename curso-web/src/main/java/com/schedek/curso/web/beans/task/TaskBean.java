package com.schedek.curso.web.beans.task;

import com.schedek.curso.ejb.entities.Group;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.TaskFinishState;
import com.schedek.curso.ejb.enums.TaskState;

import static com.schedek.curso.ejb.enums.TaskState.DONE;
import static com.schedek.curso.ejb.enums.TaskState.IN_PROGRESS;
import static com.schedek.curso.ejb.enums.TaskState.WITH_PROBLEM;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.GroupFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.ejb.facade.SubtaskFacade;
import com.schedek.curso.ejb.facade.TaskFacade;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.web.beans.list.JPADataModel;
import com.schedek.curso.web.beans.sess.SessionBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.LazyDataModel;
import util.FacesUtil;

@Named(value = "taskBean")
@ViewScoped
public class TaskBean implements Serializable {

    @EJB
    TaskFacade ef;
    @EJB
    ActivityFacade af;
    @EJB
    GroupFacade gf;
    @EJB
    SubtaskFacade stf;
    @EJB
    ListingFacade lif;
    LazyDataModel<Task> tasks;
    @Inject
    SessionBean sb;
    @Inject
    private TaskEditBean teb;

    private int tabIndex;
    private List<Task> tasksList;
    private Task task;
    private String taskStr;
    private String roleStr;
    private Group role;
    private TaskState taskState;
    private Task currentTask;
    boolean showMine = false;
    private Listing copyLis = null;

    private Date plannedOnFilter;
    private Date deadlineFilter;
    private Date finishedOnFilter;

    public TaskBean() {
    }

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (taskStr != null) {
                taskState = TaskState.valueOf(taskStr);
            } else {
                taskState = IN_PROGRESS;
            }
            if (roleStr != null) {
                role = gf.findByName(roleStr);
            }
        }
    }

    public boolean isAssigneeOrAssigneeRole(Task t) {
        return (t.getAssignee() != null ? sb.getUser().equals(t.getAssignee()) : false) || (t.getAssigneeRole() != null ? sb.getUser().isRole(t.getAssigneeRole().getName()) : false);
    }

    public Date getPlannedOnFilter() {
        return plannedOnFilter;
    }

    public void setPlannedOnFilter(Date plannedOnFilter) {
        this.plannedOnFilter = plannedOnFilter;
    }

    public Date getDeadlineFilter() {
        return deadlineFilter;
    }

    public void setDeadlineFilter(Date deadlineFilter) {
        this.deadlineFilter = deadlineFilter;
    }

    public Date getFinishedOnFilter() {
        return finishedOnFilter;
    }

    public void setFinishedOnFilter(Date finishedOnFilter) {
        this.finishedOnFilter = finishedOnFilter;
    }

    public void onCellEdit(Task t) {
        ef.edit(t);
    }

    public List<Group> getGroupsList() {
        return gf.findAll();
    }

    public LazyDataModel<Task> getTasks() {
        if (tasks == null) {
            tasks = new JPADataModel<Task>(ef) {
                @Override
                protected QueryBuilder getQueryBuilder() {
                    Boolean finished = null;
                    Boolean problem = null;
                    if (taskState.equals(IN_PROGRESS)) {
                        finished = false;
                        problem = false;
                    }
                    if (taskState.equals(DONE)) {
                        finished = true;
                        problem = false;
                    }
                    if (taskState.equals(WITH_PROBLEM)) {
                        problem = true;
                    }
                    return ef.qbTaskByFinishedAndProblem(finished, problem, showMine ? sb.getUser() : null, role, plannedOnFilter, deadlineFilter, finishedOnFilter);
                }
            };

        }
        return tasks;
    }

    public String getRoleStr() {

        return roleStr;
    }

    public void setRoleStr(String roleStr) {

        this.roleStr = roleStr;
    }

    public Task getDelete() {
        return null;
    }

    public void setDelete(Task delete) {
        try {
            if (stf.findByTask(delete).isEmpty()) {
                ef.remove(delete);
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('deleteDlg').show();");
                setTask(delete);
            }
        } catch (Exception e) {
            FacesUtil.processDbException("Delete failed", e);
        }
    }

    public Task getForcedDelete() {
        return null;
    }

    public void setForcedDelete(Task delete) {
        try {
            ef.remove(delete);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (Exception e) {
            FacesUtil.processDbException("Delete failed", e);
        }
    }

    public void setReview(Task t) {
        ef.review(t, sb.getUser());
    }

    public boolean isShowMine() {
        return showMine;
    }

    public void setShowMine(boolean showMine) {
        this.showMine = showMine;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public List<Task> getTasksList() {
        if (tasksList == null) {
            tasksList = ef.findAll();
        }
        return tasksList;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void listenerTask(AjaxBehaviorEvent abe) {
        ef.edit(task);
    }

    public String getTaskStr() {
        if (taskStr == null) {
            taskStr = "IN_PROGRESS";
        }
        return taskStr;
    }

    public void setTaskStr(String taskStr) {
        this.taskStr = taskStr;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public int getTaskCount(TaskState taskState) {
        if (taskState.equals(IN_PROGRESS)) {
            return ef.qbTaskByFinishedAndProblem(false, false, showMine ? sb.getUser() : null, role, plannedOnFilter, deadlineFilter, finishedOnFilter).loadAll().size();
        } else if (taskState.equals(WITH_PROBLEM)) {
            return ef.qbTaskByFinishedAndProblem(false, true, showMine ? sb.getUser() : null, role, plannedOnFilter, deadlineFilter, finishedOnFilter).loadAll().size();
        }
        return ef.qbTaskByFinishedAndProblem(true, false, showMine ? sb.getUser() : null, role, plannedOnFilter, deadlineFilter, finishedOnFilter).loadAll().size();
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public void listenerTaskUser(AjaxBehaviorEvent abe) {
        ef.edit(currentTask);
    }

    public String copyTask() {
        Task c = currentTask;
        Listing l = copyLis;
        Task copyTask = ef.copy(c, copyLis, sb.getUser());
        copyTask.setName(c.getName());
        copyTask.setDescription(c.getDescription());
        ef.edit(copyTask);
        ef.flush();

        return "edit.xhtml?faces-redirect=true&id=" + copyTask.getId();
    }

    public Listing getCopyLis() {
        return copyLis;
    }

    public void setCopyLis(Listing copyLis) {
        this.copyLis = copyLis;
    }

    public List<Listing> getListings() {
        return lif.findAll();
    }

    public void moveToDone(Task t) {
        teb.setTaskID(t.getId().toString());
        teb._init();
        teb.getC().setFinishedState(TaskFinishState.DONE);
        teb.finish();
    }
}
