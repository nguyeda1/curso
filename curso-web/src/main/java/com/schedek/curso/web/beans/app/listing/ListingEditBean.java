package com.schedek.curso.web.beans.app.listing;

import com.schedek.curso.ejb.entities.*;
import com.schedek.curso.ejb.enums.BookingState;
import com.schedek.curso.ejb.enums.BookingType;
import com.schedek.curso.ejb.enums.TaskState;
import com.schedek.curso.ejb.facade.*;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.ejb.files.Filestore;
import com.schedek.curso.ejb.files.UploadType;
import com.schedek.curso.ejb.util.BigDec;
import com.schedek.curso.ejb.view.ListingBookingOverview;
import com.schedek.curso.web.beans.app.EnumBean;
import com.schedek.curso.web.beans.list.JPADataModel;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.DateViewChangeEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.DefaultStreamedContent;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.LinkedList;
import util.IconResolver;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;

@Named(value = "listingEditBean")
@ViewScoped
public class ListingEditBean implements Serializable {

    @Inject
    private SessionBean sb;
    @EJB
    ListingFacade cf;
    @EJB
    BookingFacade bf;
    @EJB
    UserFacade uf;
    @EJB
    Filestore fs;
    @EJB
    LocalityFacade lof;
    @Inject
    IconResolver ir;
    @EJB
    TaskFacade tf;
    @Inject
    private EnumBean eb;
    ScheduleModel model = null;
    Long laundryAddCount = 1L;
    Long laundryRemoveCount = 1L;
    List<User> operators;
    List<ListingBookingOverview> listingBookingoverview;
    Date month = new Date();
    LineChartModel overviewLineModel;

    LazyDataModel<Task> tasks;

    private TaskState taskState;
    private Listing c;
    private String listingID;
    private String taskText;
    private File image;
    private List<Locality> localities;
    private User operator;
    private List<BookingType> bookingTypes;



    private int selectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private int selectedYear = Calendar.getInstance().get(Calendar.YEAR);
    private int activeTab = 0;

    public ListingEditBean() {
    }

    public void init() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (listingID != null) {
                c = cf.find(Long.parseLong(listingID));
                if (c == null) {
                    throw new UnsupportedOperationException();
                }
                month = new Date();
                month = new Date(month.getYear(), month.getMonth(), 1);
                listingBookingoverview = cf.getListingBookingOverviewByMonth(month, c);
            }
            if (c == null) {
                c = new Listing();
            }
        }
    }

    public String getListingID() {
        return listingID;
    }

    public void setListingID(String listingID) {
        this.listingID = listingID;
    }

    public Listing getC() {
        return c;
    }



//    public List<String> getImages() {
//        List<File> files = getImageFiles();
//        List<String> images = new ArrayList<>();
//        if (files != null) {
//            for (File file : files) {
//                images.add(file.getName());
//            }
//        }
//        return images;
//    }

    public String finish() {
        if (save() == null) {
            return null;
        }
        return "view.xhtml?id=" + c.getId() + "&activeTab=" + activeTab + "&faces-redirect=true";
    }

    public String save() {
        if (c.getId() == null) {
            cf.create(c);
        } else {
            cf.edit(c);
        }
        return "edit.xhtml?faces-redirect=true&id=" + c.getId() + "";
    }


    public List<Locality> getLocalities() {
        if (localities == null) {
            localities = lof.findAll();
        }
        return localities;
    }

    public void setLocalities(List<Locality> localities) {
        this.localities = localities;
    }

}
