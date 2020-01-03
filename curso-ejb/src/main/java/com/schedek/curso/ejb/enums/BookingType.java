package com.schedek.curso.ejb.enums;

public enum BookingType {
	AIRBNB, BOOKING, OWNER, OTHER, SERVICE,BUSINESS;

	public boolean isOwner() {
		return equals(OWNER);
	}
	public boolean isAirbnb() {
		return equals(AIRBNB);
	}
	public boolean isOther() {
		return equals(OTHER);
	}
	public boolean isBusiness() {
		return equals(BUSINESS);
	}
	public boolean isBooking() {
		return equals(BOOKING);
	}
	public boolean isService() {
		return equals(SERVICE);
	}
	
	public String getName(){
		switch (this) {
			case OWNER:
				return "Owner booking";
			case AIRBNB:
				return "AirBNB booking";
			case OTHER:
				return "Other booking";
			case BOOKING:
				return "Booking.com booking";
			case SERVICE:
				return "Service booking";
			case BUSINESS:
				return "Business booking";
			default:
				throw new AssertionError();
		}
	}
	public String getShortName(){
		switch (this) {
			case OWNER:
				return "Owner";
			case AIRBNB:
				return "AirBNB";
			case OTHER:
				return "Other";
			case BOOKING:
				return "Booking";
			case SERVICE:
				return "Service";
			case BUSINESS:
				return "Business";
			default:
				throw new AssertionError();
		}
	}
}
