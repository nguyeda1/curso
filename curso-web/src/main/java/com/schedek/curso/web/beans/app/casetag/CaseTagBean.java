package com.schedek.curso.web.beans.app.casetag;

import com.schedek.curso.ejb.entities.CaseTag;
import com.schedek.curso.ejb.facade.CaseTagFacade;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.web.beans.list.JPADataModel;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

import org.primefaces.model.LazyDataModel;
import util.FacesUtil;

@Named(value = "caseTagBean")
@ViewScoped
public class CaseTagBean implements Serializable {
    @EJB
    CaseTagFacade ef;
    LazyDataModel<CaseTag> caseTags;
    private boolean showDisabled = false;

    public CaseTagBean() {
    }

    public LazyDataModel<CaseTag> getCaseTags() {
        if (caseTags == null) {
            caseTags = new JPADataModel(ef) {

                @Override
                protected QueryBuilder getQueryBuilder() {
                    if (showDisabled) {
                        return ef.qbActive(false);
                    }
                    return ef.qbActive(true);
                }
            };
        }
        return caseTags;
    }

    public CaseTag getDelete() {
        return null;
    }

    public void setDelete(CaseTag delete) {
        try {
            ef.remove(delete);
        } catch (Exception e) {
            FacesUtil.processDbException("Delete failed", e);
        }
    }

    public boolean isShowDisabled() {
        return showDisabled;
    }

    public void setShowDisabled(boolean showDisabled) {
        this.showDisabled = showDisabled;
    }
}
