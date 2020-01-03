package com.schedek.curso.web.beans.app.booking;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.entities.Listing;
import com.schedek.curso.ejb.enums.BookingState;
import com.schedek.curso.ejb.facade.BookingFacade;
import com.schedek.curso.ejb.facade.ListingFacade;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import com.schedek.curso.web.beans.list.JPADataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.LazyDataModel;
import util.FacesUtil;

@Named(value = "bookingBean")
@ViewScoped
public class BookingBean implements Serializable {

	@EJB
	BookingFacade ef;
	@EJB
	ListingFacade lf;
	LazyDataModel<Booking> bookings;
	private List<BookingState> states = new ArrayList<>(Arrays.asList(new BookingState[]{
		BookingState.NEW,
		BookingState.APPROVED,
		BookingState.ASSIGNED,
		BookingState.GUEST_IN,
		BookingState.GUEST_OUT,
                BookingState.REVIEW,
                BookingState.ACCOUNTING,
                BookingState.FINISH
                  
	}));
	private String listingID;
	private Listing l;
	private boolean reviewsFilter = false;

	public BookingBean() {
	}

	public void init() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			if (listingID != null) {
				l = lf.find(Long.parseLong(listingID));
			}
		}
                
	}

	public LazyDataModel<Booking> getBookings() {
		if (bookings == null) {
			bookings = new JPADataModel(ef) {

				@Override
				protected QueryBuilder getQueryBuilder() {
					return ef.qbFilterAll(l, states, reviewsFilter);
				}
			};
		}
		return bookings;
	}
        
	public Booking getDelete() {
		return null;
	}

	public void setDelete(Booking delete) {
		try {
			ef.remove(delete);
		} catch (Exception e) {
			FacesUtil.processDbException("Delete failed", e);
		}
	}

	public String getListingID() {
		return listingID;
	}

	public void setListingID(String listingID) {
		this.listingID = listingID;
	}

	public Listing getL() {
		return l;
	}

	public List<BookingState> getStates() {
		return states;
	}

	public void setStates(List<BookingState> states) {
		this.states = states;
	}

	public boolean isReviewsFilter() {
		return reviewsFilter;
	}

	public void setReviewsFilter(boolean reviewsFilter) {
		this.reviewsFilter = reviewsFilter;
	}

}
