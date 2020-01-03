package com.schedek.curso.ejb;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.entities.Contact;
import com.schedek.curso.ejb.entities.Group;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.entities.Locality;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.ContactType;
import com.schedek.curso.ejb.facade.BookingFacade;
import com.schedek.curso.ejb.facade.ContactFacade;
import com.schedek.curso.ejb.facade.GroupFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.ejb.facade.LocalityFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.ejb.util.Security;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Startup
@Singleton(name = "curso")
@TransactionManagement(TransactionManagementType.BEAN)
public class AppEJB implements Serializable {

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;
    @Resource
    private EJBContext context;

    @EJB
    private DataImport importer;
    @EJB
    GroupFacade gf;
    @EJB
    LocalityFacade lof;
    @EJB
    ListingFacade lf;
    @EJB
    ContactFacade cf;
    @EJB
    UserFacade uf;

    @EJB
    BookingFacade bf;
    private AppConfig config = new AppConfig();

    @PostConstruct
    public void initialize() {
        if (count(User.class) == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
            Logger.getLogger(AppEJB.class.getName()).log(Level.INFO, "=== Starting DATA IMPORT PROCESS at " + sdf.format(new Date()), new Object[]{});
            importer.doImport();
            Logger.getLogger(AppEJB.class.getName()).log(Level.INFO, "=== Finished DATA IMPORT PROCESS at " + sdf.format(new Date()), new Object[]{});
        }

        importer.importViews();

        Group adminGroup = gf.findByName("admin");
        User admin = uf.findOneByUsername("admin");

        if (adminGroup == null) {
            adminGroup = new Group();
            adminGroup.setName("admin");
            gf.create(adminGroup);
        }
        if (admin == null) {
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword("12345");
            admin.setFirstname("Pavel");
            admin.setLastname("Sedek");
            admin.setPhone("999444333");
            admin.getGroups().add(adminGroup);

            uf.create(admin);

            adminGroup.getUsers().add(admin);
            gf.edit(adminGroup);
            User op = uf.findOneByUsername("operative");
            if (op == null) {
                op = new User();
                op.setUsername("operative");
                op.setPassword("12345");
                op.setFirstname("Dan");
                op.setLastname("Nguyen");
                op.setPhone("999666555");
                uf.create(op);
            }

        }
        if (cf.count() == 0) {
            for (ContactType type : ContactType.values()) {
                Contact c = new Contact();
                c.setType(type);
                c.setUser(admin);
                cf.create(c);
            }
        }

        if (lof.count() == 0) {
            List<String> localities = new ArrayList<>(Arrays.asList("Dejvice", "Krč", "Vršovice"));
            for (String l : localities) {
                Locality locality = new Locality();
                locality.setName(l);
                lof.create(locality);
            }
        }

        if (lf.count() == 0) {
            Listing listing = new Listing();
            listing.setAddress("Polska 57, Praha 75");
            listing.setCreated(new Date());
            listing.setLocality(lof.findAll().get(0));
            listing.setName("Polska 57, Podprokvní byt 5+kk");
            lf.create(listing);
        }

    }

    private int count(Class c) {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<User> rt = cq.from(c);
        cq.select(em.getCriteriaBuilder().count(rt));
        //may do import here after executing init sql
        return ((Long) em.createQuery(cq).getSingleResult()).intValue();
    }

    public AppConfig getConfig() {
        return config;
    }

    public String getBuildDate() {
        return config.getProperty("builddate");
    }

    public String getVersion() {
        return config.getProperty("version");
    }

    public boolean isProduction() {
        return getConfig().getPrefix().equals("curso");
    }

}
