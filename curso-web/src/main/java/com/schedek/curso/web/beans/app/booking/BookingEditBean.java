package com.schedek.curso.web.beans.app.booking;

import com.schedek.curso.ejb.entities.*;
import com.schedek.curso.ejb.enums.BookingState;
import com.schedek.curso.ejb.enums.BookingType;

import com.schedek.curso.ejb.facade.BookingFacade;
import com.schedek.curso.ejb.facade.CurrencyFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.ejb.facade.*;
import com.schedek.curso.ejb.util.exceptions.BookingFacadeException;
import com.schedek.curso.ejb.util.exceptions.BookingColisionException;
import com.schedek.curso.web.beans.app.EnumBean;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "bookingEditBean")
@ViewScoped
public class BookingEditBean implements Serializable {

	@Inject
	private SessionBean sb;
	@Inject
	private EnumBean eb;
	@EJB
	BookingFacade cf;
	@EJB
	ListingFacade lf;
	@EJB
	UserFacade uf;
	@EJB
	CurrencyFacade curf;
	@EJB
	TaskFacade tf;
	private Booking c;
	private String bookingID;
	private String listingID;
	private List<BookingType> bookingTypes;
	private List<Task> t;
//    private BigDecimal bookingPaymentRequestAmount;


	//paymentRequest

	public BookingEditBean() {
	}

	public void init() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			if (bookingID != null) {
				c = cf.find(Long.parseLong(bookingID));
				t = tf.findTaskByLockboxBooking(c).loadAll();
			} else if (listingID != null) {
				Listing l = lf.find(Long.parseLong(listingID));
				Objects.requireNonNull(l);
				c = cf.factoryMethod(l);
				c.setCreatedBy(sb.getUser());
			}
			if (c == null) {
				throw new UnsupportedOperationException();
			}
			if (c.getPrice().getCurrency() == null) {
				c.getPrice().setCurrency(curf.find("CZK"));
			}
		}
	}



//    public void removePdf() {
//        bprf.rmfiles(getPaymentRequest());
//
//    }
	public String getBookingID() {
		return bookingID;
	}

	public void setBookingID(String bookingID) {
		this.bookingID = bookingID;
	}

	public List<BookingType> getBookingTypes() {
		if (sb.getUser().isAdmin() || sb.getUser().isOperator() || sb.getUser().isListingsManager() || sb.getUser().isOperationsManagement()) {
			bookingTypes = eb.getBookingTypes();
		} else {
			bookingTypes = new LinkedList<>();
			bookingTypes.add(BookingType.OWNER);
			bookingTypes.add(BookingType.OTHER);
			bookingTypes.add(BookingType.BUSINESS);
			bookingTypes.add(BookingType.SERVICE);
		}
		return bookingTypes;
	}

	public void setBookingTypes(List<BookingType> bookingTypes) {
		this.bookingTypes = bookingTypes;
	}

	public String getListingID() {
		return listingID;
	}

	public void setListingID(String listingID) {
		this.listingID = listingID;
	}

	public Booking getC() {
		return c;
	}

	public String approve() {
		String ret = finish();
		if (ret != null) {
			ret = "list-review.xhtml?faces-redirect=true";
		}
		return ret;

	}

	public String finish() {
		if (c.getId() != null) {
			Booking b = cf.find(c.getId());
			if ((BookingState.ACCOUNTING.equals(b.getState())
					&& !BookingState.FINISH.equals(c.getState()))
					|| BookingState.FINISH.equals(b.getState())) {
				c.setState(b.getState());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Booking state could not be reverted, ignoring"));
			}
		}
		if (save() != null) {
			return "index.xhtml?faces-redirect=true";
		} else {
			return null;
		}
	}

	public String finishComlog() {
		save();
		return "comlog.xhtml?faces-redirect=true&id=" + c.getId();
	}

	public String save() {
		boolean ok = true;
		if (c.getGuestCount() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid guest count"));
			ok = false;
		} 
		if (c.getStartDate() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Start date is required"));
			ok = false;
		}
		if (c.getEndDate() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("End date is required"));
			ok = false;
		}
		if (c.getStartDate() != null && c.getEndDate() != null && !c.getEndDate().after(c.getStartDate())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("End date must be after start date"));
			ok = false;
		}
		if (!ok) {
			return null;
		}
		try {
			c.getPrice().setPrice(c.getPrice().getPrice().setScale(2, RoundingMode.DOWN));
			if (c.getId() == null) {
				cf.create(c);
			} else {
				cf.edit(c);
			}
			return "edit.xhtml?faces-redirect=true&id=" + c.getId() + "";
		} catch (BookingColisionException | BookingFacadeException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage(), ""));
		}
		return null;
	}


	public Date getStartDate() {
		return c.getStartDate();
	}

	public Date getStartTime() {
		return c.getStartTime();
	}

	public void setStartDate(Date startDate) {

		c.setStartDate(startDate);

	}

	public void setStartTime(Date startTime) {

		c.setStartTime(startTime);

	}

	public static boolean timeCompareSpecial(Date booking, Date holder) {

		Calendar call = Calendar.getInstance();
		call.setTime(booking);
		Calendar call2 = Calendar.getInstance();
		call2.setTime(holder);

		if (call.get(Calendar.HOUR_OF_DAY) + 2 < call2.get(Calendar.HOUR_OF_DAY)) {
			return true;
		} else if (call.get(Calendar.HOUR_OF_DAY) + 2 == call2.get(Calendar.HOUR_OF_DAY)) {
			if (call.get(Calendar.MINUTE) <= call2.get(Calendar.MINUTE)) {
				return true;
			}
		}
		return false;
	}




	public String delete() {
		if (c.getId() != null) {
			cf.cancel(c, sb.getOriginalUser() == null ? sb.getUser() : sb.getOriginalUser());
		}
		return "index.xhtml?faces-redirect=true";
	}

	public boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}


	public boolean hasSimilarGuestName() {
		return cf.getBookingsWithSameGuestName(c).size() > 1;
	}
}
