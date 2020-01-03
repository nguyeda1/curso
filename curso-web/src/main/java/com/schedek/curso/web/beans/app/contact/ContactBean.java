/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.web.beans.app.contact;

import com.schedek.curso.ejb.entities.Contact;
import com.schedek.curso.ejb.entities.Group;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.ContactType;
import com.schedek.curso.ejb.facade.ContactFacade;
import com.schedek.curso.ejb.facade.GroupFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Dan Nguyen
 */
@Named(value = "contactBean")
@ViewScoped
public class ContactBean implements Serializable {

    @Inject
    private SessionBean sb;
    @EJB
    private UserFacade uf;
    @EJB
    private GroupFacade gf;
    @EJB
    private ContactFacade cf;
    ArrayList<Contact> contactList;
    List<User> userList;
    List<User> receptionList;
    List<User> listingsManagerList;
    List<User> cleaningList;
    List<User> communicationList;
    List<User> internalSalesList;

    public static String open() {
        return "/app/contact/index.xhtml?faces-redirect=true";
    }

    public List<User> getUserList() {
        if (userList == null) {
            userList = uf.findAll();
        }
        return userList;
    }

    public List<User> listUsers(ContactType t) {
        return getUserList();
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Contact> getContactList() {
        if (contactList == null) {
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
        }
        return contactList;
    }

    public void save() {
        if (contactList != null) {
            for (Contact c : contactList) {
                if (c.getId() == null) {
                    cf.create(c);
                } else {
                    cf.edit(c);
                }
            }
        }
        cf.flush();
    }

}
