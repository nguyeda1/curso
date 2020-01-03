/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author Dan Nguyen
 */
@Entity
public class TaskActivity extends Activity {

    @ManyToOne
    private Task task;
    

    public TaskActivity() {
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    
    
}