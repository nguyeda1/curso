package com.schedek.curso.web.beans.app.cases;

import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseFeedback;
import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.entities.Group;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.enums.CaseRole;
import com.schedek.curso.ejb.enums.CaseState;
import com.schedek.curso.ejb.facade.CaseFacade;
import com.schedek.curso.ejb.facade.CaseFeedbackFacade;
import com.schedek.curso.ejb.facade.GroupFacade;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.web.beans.list.JPADataModel;
import com.schedek.curso.web.beans.sess.SessionBean;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import util.FacesUtil;

@Named(value = "caseBean")
@ViewScoped
public class CaseBean implements Serializable {

    @EJB
    CaseFacade ef;
    @EJB
    CaseFeedbackFacade eff;
    @EJB
    GroupFacade gf;
    LazyDataModel<Case> cases;
    @Inject
    SessionBean sb;
    CaseState caseState;
    boolean showMine = false;
    boolean ownedByMe = false;
    boolean myListings = false;
    String caseStr;
    private Case selectedCase;
    private List<CaseTag> tags = null;
    private CaseRole caseRole = CaseRole.MYREVIEWS;
    private CaseFeedback guestFeedback = new CaseFeedback();
    private CaseFeedback hostFeedback = new CaseFeedback();
    private Group group;
    private String template;
    private Boolean tmpt;
    private String roleStr;
    private Group role;
    private Boolean userIsRole;

    public CaseBean() {
    }

    public CaseFeedback getGuestFeedback() {
        return guestFeedback;
    }

    public void setGuestFeedback(CaseFeedback guestFeedback) {
        this.guestFeedback = guestFeedback;
    }

    public CaseFeedback getHostFeedback() {
        return hostFeedback;
    }

    public void setHostFeedback(CaseFeedback hostFeedback) {
        this.hostFeedback = hostFeedback;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void resetFeedback() {
        guestFeedback = new CaseFeedback();
        hostFeedback = new CaseFeedback();
    }

    public Case getSelectedCase() {
        return selectedCase;
    }

    public void setSelectedCase(Case selectedCase) {
        this.selectedCase = selectedCase;
        guestFeedback.setCursoCase(selectedCase);
        hostFeedback.setCursoCase(selectedCase);
    }

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (roleStr != null && !roleStr.equals("null")) {
                role = gf.findByName(roleStr);
            }
            if (template != null && template.equals("true")) {
                tmpt = true;
            } else if (caseStr != null && !caseStr.startsWith("null")) {
                String cstr = caseStr;
                if (caseStr.endsWith("_MINE")) {
                    cstr = caseStr.substring(0, caseStr.length() - 5);
                    showMine = true;
                }
                caseState = CaseState.valueOf(cstr);
            } else {
                caseState = CaseState.NEW;
            }
        }
    }

    public List<Group> getGroupsList() {
        return gf.findByDepartment(true);
    }

    public LazyDataModel<Case> getCases() {
        if (cases == null) {
            if (caseState == null && tmpt == null) {
                throw new UnsupportedOperationException();
            }
            cases = new JPADataModel(ef) {
                @Override
                protected QueryBuilder getQueryBuilder() {
                    if (tmpt != null && tmpt == true) {
                        return ef.qbTemplates(tmpt);
                    }
                    List<Group> groups = new ArrayList();
                    groups.add(group);
//                    return ef.qbCaseByStateAssigneeTagsOwnerMyListings(caseState, showMine ? sb.getUser() : null,
//                            tags, ownedByMe || (caseState.isReview() && caseRole.isMyReviews()) ? sb.getUser() : null, myListings ? sb.getUser() : null, caseState.isReview() ? caseRole : null, group == null ? sb.getUser().getGroups() : groups, role);
                    return ef.qbCaseByStateAssigneeTagsOwnerMyListings(caseState, showMine ? sb.getUser() : null,
                            tags, ownedByMe || (caseState.isReview() && caseRole.isMyReviews()) ? sb.getUser() : null, myListings ? sb.getUser() : null, caseState.isReview() ? caseRole : null,null, role);
                }
            };
        }
        return cases;
    }

    public CaseRole getCaseRole() {
        return caseRole;
    }

    public void setCaseRole(CaseRole caseRole) {
        this.caseRole = caseRole;
    }

    public String getRoleStr() {

        return roleStr;
    }

    public void setRoleStr(String roleStr) {
        this.roleStr = roleStr;
    }

    public Group getRole() {
        return role;
    }

    public void setRole(Group role) {
        this.role = role;
    }

    public Boolean getUserIsRole() {
        if (roleStr == null || roleStr.equals("null")) {
            return Boolean.FALSE;
        }
        if (userIsRole == null) {
            User u = sb.getUser();
            userIsRole = u.isRole(roleStr);
        }
        return userIsRole;

    }

    public void setUserIsRole(Boolean userIsRole) {
        this.userIsRole = userIsRole;
    }

    private Map<CaseState, Integer> qmc = new HashMap<CaseState, Integer>();

    public int getCaseCount(CaseState caseState) {
        List groups = new ArrayList();
        groups.add(group);
//        return ef.qbCaseByStateAssigneeTagsOwnerMyListings(caseState, showMine ? sb.getUser() : null,
//                tags, ownedByMe || (caseState.isReview() && caseRole.isMyReviews()) ? sb.getUser() : null, myListings ? sb.getUser() : null, caseState.isReview() ? caseRole : null, group == null ? sb.getUser().getGroups() : groups, role).loadAll().size();
        return ef.qbCaseByStateAssigneeTagsOwnerMyListings(caseState, showMine ? sb.getUser() : null,
                tags, ownedByMe || (caseState.isReview() && caseRole.isMyReviews()) ? sb.getUser() : null, myListings ? sb.getUser() : null, caseState.isReview() ? caseRole : null, null, role).loadAll().size();
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

    public void in_progress(Case c) {
        c.setCaseState(CaseState.IN_PROGRESS);
        ef.edit(c);

    }

    public boolean isReviewState() {
        return caseState != null && caseState.isReview();
    }

    public void done() {
        try {
            if (!(guestFeedback.getType() == null)) {
                if (!(guestFeedback.getType().isNone())) {
                    guestFeedback.setReviewedBy(sb.getUser());
                    guestFeedback.setReviewedOn(new Date());
                    guestFeedback.setGuestInformed(true);
                    if (guestFeedback.getType().isNone()) {
                        guestFeedback.setDescription("");
                    }
                } else {
                    guestFeedback = null;
                }
            } else {
                guestFeedback = null;
            }

            if (!(hostFeedback.getType() == null)) {
                if (!(hostFeedback.getType().isNone())) {
                    hostFeedback.setReviewedBy(sb.getUser());
                    hostFeedback.setReviewedOn(new Date());
                    hostFeedback.setHostInformed(true);
                    if (hostFeedback.getType().isNone()) {
                        hostFeedback.setDescription("");
                    }
                } else {
                    hostFeedback = null;
                }
            } else {
                hostFeedback = null;
            }
        } catch (EJBException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Description is too long"));
        }

        selectedCase.setCaseState(CaseState.DONE);
        selectedCase.setFinishedOn(new Date());
        selectedCase.setFinishedBy(sb.getUser());

        ef.edit(selectedCase);

        if (guestFeedback != null) {
            eff.create(guestFeedback);
        }
        if (hostFeedback != null) {
            eff.create(hostFeedback);
        }

        guestFeedback = new CaseFeedback();
        hostFeedback = new CaseFeedback();
    }

    public String getCaseStr() {
        return caseState == null ? null : caseState.name();
    }

    public void setCaseStr(String caseStr) {
        this.caseStr = caseStr;
    }

    public List<CaseTag> getTags() {
        return tags;
    }

    public void setTags(List<CaseTag> tags) {
        this.tags = tags;
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

    public boolean isShowMine() {
        return showMine;
    }

    public void setShowMine(boolean showMine) {
        this.showMine = showMine;
    }

    public boolean isOwnedByMe() {
        return ownedByMe;
    }

    public void setOwnedByMe(boolean ownedByMe) {
        this.ownedByMe = ownedByMe;
    }

    public boolean isMyListings() {
        return myListings;
    }

    public void setMyListings(boolean myListings) {
        this.myListings = myListings;
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
    
    public void changeDepartment() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/app/case/index.xhtml?caseState=" + caseStr + "&role=" + roleStr);
    }
}
