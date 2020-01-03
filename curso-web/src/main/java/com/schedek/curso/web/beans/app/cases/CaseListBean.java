package com.schedek.curso.web.beans.app.cases;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.web.beans.list.JPADataModel;
import com.schedek.curso.web.beans.sess.SessionBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import util.FacesUtil;

@Named(value = "caseListBean")
@ViewScoped
public class CaseListBean implements Serializable {

    @EJB
    CaseFacade ef;
    LazyDataModel<Case> cases;
    @Inject
    SessionBean sb;
    CaseState caseState;
    boolean showMine = false;
    String caseStr;
    String listingID;
    @EJB
    ListingFacade cf;
    private Listing listing = null;

    public CaseListBean() {
    }

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {

            if (caseStr != null) {
                String cstr = caseStr;
                if (caseStr.endsWith("_MINE")) {
                    cstr = caseStr.substring(0, caseStr.length() - 5);
                    showMine = true;
                }
                caseState = CaseState.valueOf(cstr);
            }
            if (listingID != null) {
                listing = cf.find(Long.parseLong(listingID));
                if (listing == null) {
                    throw new UnsupportedOperationException();
                }

            }
        }
    }

    public void assignToMe(Case c) {
        c.setAssignee(sb.getUser());
        c.setCaseState(CaseState.IN_PROGRESS);
        c.setAssigned(new Date());

        ef.edit(c);

    }

    public void review(Case c) {
        c.setCaseState(CaseState.REVIEW);
        ef.edit(c);

    }

    public void done(Case c) {
        c.setCaseState(CaseState.DONE);
        ef.edit(c);

    }

    public String getCaseStr() {
        return caseState == null ? null : caseState.name();
    }

    public void setCaseStr(String caseStr) {
        this.caseStr = caseStr;
    }

    public String getListingID() {
        return listingID;
    }

    public void setListingID(String listingID) {
        this.listingID = listingID;
    }

    public CaseState getCaseState() {
        return caseState;
    }

    public String getCaseStateReturn() {
        return caseState + (showMine ? "_MINE" : "");
    }

    public void setCaseState(CaseState caseState) {
        this.caseState = caseState;
    }

    public LazyDataModel<Case> getCases() {
        if (cases == null) {
//            if (caseState == null) {
//                throw new UnsupportedOperationException();
//            }
            if (listing == null) {
                throw new UnsupportedOperationException();
            }
            cases = new JPADataModel(ef) {
                @Override
                protected QueryBuilder getQueryBuilder() {
                    return ef.qbCaseByCaseStateAndListing(caseState, showMine ? sb.getUser() : null, listing);
                }
            };

        }
        return cases;
    }

    public boolean isShowMine() {
        return showMine;
    }

    public void setShowMine(boolean showMine) {
        this.showMine = showMine;
    }

    public Case getDelete() {
        return null;
    }

    public void setDelete(Case delete) {
        try {
            ef.remove(delete);
        } catch (Exception e) {
            FacesUtil.processDbException("Delete failed", e);
        }
    }

}
