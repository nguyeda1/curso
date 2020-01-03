/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 *
 * @author Tomáš
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(MultiPartFeature.class);
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.schedek.curso.taskmanager.app.controllers.ActivityController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.BookingController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.CaseController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.CaseFeedbackController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.CaseTagController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.CommentController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.ContactController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.InitController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.ListingController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.SubtaskController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.TaskController.class);
        resources.add(com.schedek.curso.taskmanager.app.controllers.UserController.class);
    }
    
}
