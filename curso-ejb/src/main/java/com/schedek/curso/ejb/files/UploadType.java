package com.schedek.curso.ejb.files;

import com.schedek.curso.ejb.entities.*;

import java.io.File;
import javax.ejb.EJBException;

public enum UploadType {

    INVOICE_IN,
    LISTING_INVOICE,
    LISTING_MAP,
    BOOKING_ROUTE_GUIDE,
    BOOKING_FILE,
    INVOICE_OUT,
    BOOKING_CONTRACT,
    BOOKING_CONTRACT_ADDON,
    BOOKING_PAYMENT_REQUEST,
    LISTING_FILES,
    LISTING_FILES_ADMIN,
    INVOICE_OUT_SUM,
    DPH_KH, DPH_DP,
    CONTRACT_DPC,
    CONTRACT_DPC_SIGNED,
    CONTRACT_DPP,
    CONTRACT_DPP_SIGNED,
    CONTRACT_NDA,
    CONTRACT_NDA_SIGNED,
    PERSON_PAYOUT,
    PERSON_PAYOUT_SIGNED,
    PROHLASENI_POPLATNIKA,
    LISTING_EQUIPMENT,
    LISTING_DAMAGE,
    PERSON_IMAGE,
    WEBSITE_IMAGE,
    TASK_IMAGE,
    CASE_IMAGE,
    VEHICLE_FILE,
    CLAIM_FILES,
    MESSAGE_POST_ATTACHMENT,
    VEHICLE_DAMAGE_PHOTO;

    String dirName(Object entity) {
        //files not related to any object
        switch (this) {
            case LISTING_INVOICE:
                if (!(entity instanceof Listing)) {
                    throw new EJBException("Listing file requires Listing entity");
                }
                Listing j = (Listing) entity;
                return "listing" + File.separator + j.getId() + File.separator + "invoice";
            case LISTING_MAP:
                if (!(entity instanceof Listing)) {
                    throw new EJBException("Listing file requires Listing entity");
                }
                Listing m = (Listing) entity;
                return "listing" + File.separator + m.getId() + File.separator + "map";
            case BOOKING_ROUTE_GUIDE:
                if (!(entity instanceof Booking)) {
                    throw new EJBException("Booking file requires booking entity");
                }
                Booking w = (Booking) entity;
                return "booking" + File.separator + w.getId() + File.separator + "route_guide";
            case BOOKING_FILE:
                if (!(entity instanceof Booking)) {
                    throw new EJBException("Booking file requires booking entity");
                }
                Booking bi = (Booking) entity;
                return "booking" + File.separator + bi.getId() + File.separator + "booking_file";
            case LISTING_FILES:
                if (!(entity instanceof Listing)) {
                    throw new EJBException("Listing file requires Listing entity");
                }
                Listing k = (Listing) entity;
                return "listing" + File.separator + k.getId() + File.separator + "files";
            case LISTING_FILES_ADMIN:
                if (!(entity instanceof Listing)) {
                    throw new EJBException("Listing file requires Listing entity");
                }
                Listing l = (Listing) entity;
                return "listing" + File.separator + l.getId() + File.separator + "files_admin";
            case WEBSITE_IMAGE:
                if (!(entity instanceof Listing)) {
                    throw new EJBException("Entity is not INSTANCE of Listing");
                }
                Listing listing = (Listing) entity;
                return "listing" + File.separator + listing.getId();

            case TASK_IMAGE:
                if (!(entity instanceof Task)) {
                    throw new EJBException("Entity is not INSTANCE of TASK_IMAGE");
                }
                Task task = (Task) entity;
                return "task" + File.separator + task.getId();
            case CASE_IMAGE:
                if (!(entity instanceof Case)) {
                    throw new EJBException("Entity is not INSTANCE of CASE_IMAGE");
                }
                Case c = (Case) entity;
                return "case" + File.separator + c.getId();
        }

        throw new EJBException(
                "Invalid upload type");

    }
}
