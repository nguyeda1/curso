/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.controllers;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.entities.Subtask;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.TaskComment;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.CaseTagFacade;
import com.schedek.curso.ejb.facade.SubtaskFacade;
import com.schedek.curso.ejb.facade.TaskCommentFacade;
import com.schedek.curso.ejb.facade.TaskFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Dan Nguyen
 */
@Path("/init")
@ApplicationScoped
@Named
public class InitController {

    @EJB
    CaseFacade cf;
    @EJB
    TaskFacade tf;
    @EJB
    UserFacade uf;
    @EJB
    CaseTagFacade ctf;
    @EJB
    TaskCommentFacade tcf;
    @EJB
    SubtaskFacade stf;

    @GET
    public void init() {

//       User u = uf.findOneByUsername("denky");
//       u.setFirstname("Dan");
//       u.setLastname("Nguyen");
//       uf.edit(u);
//        
        Case c = cf.find(Long.valueOf(78));
        Task t1 = new Task("Call electrician");
        Task t2 = new Task("Clean oven");
        Task t3 = new Task("Assess the damage");
        Task t4 = new Task("Demand money from last guest");
        Task t5 = new Task("Inform host");
        Task t = new Task("Buy new oven if needed");

        t1.setCursoCase(c);
        t2.setCursoCase(c);
        t3.setCursoCase(c);
        t4.setCursoCase(c);
        t5.setCursoCase(c);
        t.setCursoCase(c);

        tf.create(t1);
        tf.create(t2);
        tf.create(t3);
        tf.create(t4);
        tf.create(t5);
        tf.create(t);

        Subtask st1 = new Subtask("Pick a store");
        Subtask st2 = new Subtask("Make list of candidate ovens");
        Subtask st3 = new Subtask("Discuss with managment");
        Subtask st4 = new Subtask("Buy oven");
        Subtask st5 = new Subtask("Deliver it to the flat");
        Subtask st7 = new Subtask("Install oven");
        Subtask st9 = new Subtask("Contact managment");
        Subtask st11 = new Subtask("Contact host");
        Subtask st12 = new Subtask("Charge guest");

        st1.setTask(t);
        st2.setTask(t);
        st3.setTask(t);
        st4.setTask(t);
        st5.setTask(t);
        st7.setTask(t);
        st9.setTask(t);
        st11.setTask(t);
        st12.setTask(t);

        stf.create(st1);
        stf.create(st2);
        stf.create(st3);
        stf.create(st4);
        stf.create(st5);
        stf.create(st7);
        stf.create(st9);
        stf.create(st11);
        stf.create(st12);

        Case c2 = cf.find(Long.valueOf(57));
        Task t12 = new Task("Call building company");
        Task t22 = new Task("Remove windows");
        Task t32 = new Task("Assess the damage");
        Task t42 = new Task("Contact managment");
        Task t52 = new Task("Inform host");
        Task tx2 = new Task("Buy new windows if needed");

        t12.setCursoCase(c2);
        t22.setCursoCase(c2);
        t32.setCursoCase(c2);
        t42.setCursoCase(c2);
        t52.setCursoCase(c2);
        tx2.setCursoCase(c2);

        tf.create(t12);
        tf.create(t22);
        tf.create(t32);
        tf.create(t42);
        tf.create(t52);
        tf.create(tx2);

        Subtask st12x = new Subtask("Pick a store");
        Subtask st22 = new Subtask("Make list of candidate windows");
        Subtask st32 = new Subtask("Discuss with managment");
        Subtask st42 = new Subtask("Buy windows");
        Subtask st52 = new Subtask("Deliver it to the flat");
        Subtask st72 = new Subtask("Install windows");
        Subtask st92 = new Subtask("Contact managment");
        Subtask st112 = new Subtask("Contact host");
        Subtask st122 = new Subtask("Charge guest");

        st12x.setTask(tx2);
        st22.setTask(tx2);
        st32.setTask(tx2);
        st42.setTask(tx2);
        st52.setTask(tx2);
        st72.setTask(tx2);
        st92.setTask(tx2);
        st112.setTask(tx2);
        st122.setTask(tx2);

        stf.create(st12x);
        stf.create(st22);
        stf.create(st32);
        stf.create(st42);
        stf.create(st52);
        stf.create(st72);
        stf.create(st92);
        stf.create(st112);
        stf.create(st122);

//        TaskComment tc1 = new TaskComment("Ahoj jak je");
//        TaskComment tc2 = new TaskComment("Dobry uklid zachod");
//        TaskComment tc3 = new TaskComment("ne ty uklid zachod");
//        TaskComment tc4 = new TaskComment("Asfasfasasfasf");
//        TaskComment tc5 = new TaskComment("I am telling mom");
//        TaskComment tc6 = new TaskComment("Comment 13548");
//
//        tc1.setTask(t);
//        tc2.setTask(t);
//        tc3.setTask(t);
//        tc4.setTask(t);
//        tc5.setTask(t);
//        tc6.setTask(t);
//
//        tcf.create(tc1);
//        tcf.create(tc2);
//        tcf.create(tc3);
//        tcf.create(tc4);
//        tcf.create(tc5);
//        tcf.create(tc6);
    }

}
