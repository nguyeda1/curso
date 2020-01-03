/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto.task;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.schedek.curso.ejb.entities.Activity;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.taskmanager.app.dto.task.TaskWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tomáš
 */
@XmlRootElement
public class TaskDetailWrapper implements Serializable {

    private TaskWrapper task;
    private Long checklistCount;
    private Long commentCount;

    public TaskDetailWrapper() {
    }

    public TaskDetailWrapper(TaskWrapper c, Long checklistCount, Long commentCount) {
        this.task = c;
        this.checklistCount = checklistCount;
        this.commentCount = commentCount;
    }

    @XmlElement
    public TaskWrapper getTask() {
        return task;
    }

    public void setTask(TaskWrapper task) {
        this.task = task;
    }

    @XmlElement
    public Long getChecklistCount() {
        return checklistCount;
    }

    public void setChecklistCount(Long checklistCount) {
        this.checklistCount = checklistCount;
    }

    @XmlElement
    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

}
