package com.schedek.curso.ejb.enums;

public enum BookingState {

	NEW,
	APPROVED,
	ASSIGNED,
	GUEST_IN,
	GUEST_OUT,
	REVIEW,
	ACCOUNTING,
	FINISH;

	public boolean isNew() {
		return equals(NEW);
	}

	public boolean isApproved() {
		return equals(APPROVED);
	}

	public boolean isAssigned() {
		return equals(ASSIGNED);
	}

	public boolean isGuestIn() {
		return equals(GUEST_IN);
	}

	public boolean isGuestOut() {
		return equals(GUEST_OUT);
	}

	public boolean isReview() {
		return equals(REVIEW);
	}

	public boolean isAccounting() {
		return equals(ACCOUNTING);
	}

	public boolean isFinish() {
		return equals(FINISH);
	}

	public String getName() {
		return ordinal() + " - " + name();
	}

	public String getCapitalCaseName() {
		switch (this) {
			case ACCOUNTING:
				return "Accounting";
			case GUEST_IN:
				return  "Guest in";
			case APPROVED:
				return "Approved";
			case GUEST_OUT:
				return "Guest out";
			case ASSIGNED:
				return "Assigned";
			case REVIEW:
				return "Review";
			case FINISH:
				return "Finish";
			case NEW:
				return "New";
				default:
				    throw new AssertionError();
		}
	}
}
