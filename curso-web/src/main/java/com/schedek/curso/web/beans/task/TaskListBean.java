package com.schedek.curso.web.beans.task;

import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.enums.TaskState;
import com.schedek.curso.ejb.facade.TaskFacade;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.web.beans.app.listing.ListingEditBean;
import com.schedek.curso.web.beans.list.JPADataModel;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

import org.primefaces.model.LazyDataModel;
import util.FacesUtil;

@Named(value = "taskListBean")
@ViewScoped
public class TaskListBean implements Serializable {

    @EJB
    TaskFacade ef;
    @Inject
    ListingEditBean leb;

    private int tabIndex;
    private LazyDataModel<Task> tasks;
    private Task task;
    private TaskState taskState;
	private String taskStr;

    public TaskListBean() {
    }

    public LazyDataModel<Task> getTasks() {
        if (tasks == null) {
            tasks = new JPADataModel(ef) {
                @Override
                protected QueryBuilder getQueryBuilder() {
                    Boolean finished = null;
					if (taskStr != null) {
                        taskState = TaskState.valueOf(taskStr);
                    }
                    if (TaskState.IN_PROGRESS.equals(taskState)) {
                        finished = false;
                    }
                    if (TaskState.DONE.equals(taskState)) {
                        finished = true;
                    }
                    return ef.qbTaskByFinishedAndListing(finished, leb.getC());
                }
            };
        }
        return tasks;
    }

    public Task getDelete() {
        return null;
    }

    public void setDelete(Task delete) {
        try {
            ef.remove(delete);
        } catch (Exception e) {
            FacesUtil.processDbException("Delete failed", e);
        }
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
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

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }
	
    public String getTaskStr() {
        return taskState == null ? null : taskState.name();
    }

    public void setTaskStr(String taskStr) {
        this.taskStr = taskStr;
    }
	    
    public String getTaskStateReturn() {
        return taskState + "";
    }
}