package com.schedek.curso.web.beans.app.listing;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.entities.Locality;
import com.schedek.curso.ejb.facade.BookingFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.ejb.facade.LocalityFacade;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.web.beans.list.JPADataModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import util.FacesUtil;

@Named(value = "listingBean")
@ViewScoped
public class ListingBean implements Serializable {

    @EJB
    ListingFacade ef;
    LazyDataModel<Listing> listings;
    LazyDataModel<Listing> terminating;
    List<Listing> listingsList;
    List<Listing> listingsListAll;
    List<Listing> selectedListingsList;
    List<SelectItem> localities;
    List<SelectItem> capacityOptions;
    private boolean showGuides = false;
    Map<Listing, Booking> lastCO = new HashMap<Listing, Booking>();
    @EJB
    BookingFacade bf;
    @EJB
    LocalityFacade lf;
    private boolean invoiceSum = false;

    private boolean showDisabled = false;

    public ListingBean() {
    }

    public LazyDataModel<Listing> getListings() {
        if (listings == null) {
            listings = new JPADataModel(ef) {
                @Override
                protected QueryBuilder getQueryBuilder() {
                    if (showDisabled) {
                        return ef.qbActive(null);
                    }
                    return ef.qbActive(true);
                }

            };
        }
        return listings;
    }

    public List<Listing> getListingsList() {
        if (listingsList == null) {
            if (showDisabled) {
                listingsList = ef.findAll();
            } else {
                listingsList = ef.findActive();
            }
        }
        return listingsList;
    }

    public Listing getDelete() {
        return null;
    }

    public void setDelete(Listing delete) {
        try {
            ef.remove(delete);
        } catch (Exception e) {
            FacesUtil.processDbException("Delete failed", e);
        }
    }

    public List<Listing> getSelectedListingsList() {
        return selectedListingsList;
    }

    public void setSelectedListingsList(List<Listing> selectedListingsList) {
        this.selectedListingsList = selectedListingsList;
    }

    public StreamedContent download(File f) throws FileNotFoundException {
        return new DefaultStreamedContent(new FileInputStream(f), f.getName().endsWith(".pdf") ? "application/pdf" : "binary/octet-stream", f.getName());
    }


    public boolean isShowDisabled() {
        return showDisabled;
    }

    public void setShowDisabled(boolean showDisabled) {
        this.showDisabled = showDisabled;
    }

    public void reset() {
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("listingBean");
    }

    public List<Listing> getListingsListAll() {
        if (listingsListAll == null) {
            listingsListAll = ef.findAll();
        }
        return listingsListAll;
    }

    public List<SelectItem> getLocalities() {
        if (localities == null) {
            localities = lf.getLocalitiesOptions();
        }
        return localities;
    }
    
    public List<SelectItem> getCapacityOptions() {
        if (capacityOptions == null) {
            capacityOptions = ef.getCapacityOptions();
        }
        return capacityOptions;
    }
    
    public LazyDataModel<Listing> getTerminating() {
        if (terminating == null) {
            terminating = new JPADataModel<>(ef);
        }
        return terminating;
    }

}
