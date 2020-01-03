/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Contact;
import com.schedek.curso.ejb.enums.ContactType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.python.google.common.base.Objects;

/**
 *
 * @author Dan Nguyen
 */
@Stateless
public class ContactFacade extends AbstractFacade<Contact> {

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ContactFacade() {
        super(Contact.class);
    }

    @Override
    public void edit(Contact entity) {
        Contact fromDb = find(entity.getId());
        super.edit(entity);
    }

    public Contact findByType(ContactType type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> cq = cb.createQuery(Contact.class);
        Root<Contact> r = cq.from(Contact.class);
        cq.where(cb.equal(r.get("type"), type));
        cq.select(r);

        List<Contact> listings = em.createQuery(cq).getResultList();
        if (!listings.isEmpty()) {
            return listings.get(0);
        }
        return null;
    }
}
