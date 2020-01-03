/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.facade;

import com.schedek.curso.ejb.entities.Group;
import com.schedek.curso.ejb.entities.User;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Root
 */
@Stateless
public class GroupFacade extends AbstractFacade<Group> {

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupFacade() {
        super(Group.class);
    }

    public List<Group> findByUser(User u) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Root<Group> prod = cq.from(Group.class);
        cq.where(cb.isMember(u, prod.get("users").as(List.class)));
        cq.select(prod);
        return em.createQuery(cq).getResultList();
    }

    public Group findByName(String gn) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Root<Group> prod = cq.from(Group.class);
        cq.where(cb.equal(prod.get("name"), gn));
        cq.select(prod);
        List<Group> rl = em.createQuery(cq).getResultList();
        if (rl.size() > 1) {
            throw new EJBException("Duplicate group " + gn);
        }
        if (rl.isEmpty()) {
            return null;
        }
        return rl.get(0);
    }

    public List<Group> findByDepartment(boolean department) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Group> r = cq.from(Group.class);
//        cq.orderBy(cb.desc(r.get("name")));
        cq.where(cb.equal(r.get("department"), department));
        cq.select(r);
        return em.createQuery(cq).getResultList();
    }

}
