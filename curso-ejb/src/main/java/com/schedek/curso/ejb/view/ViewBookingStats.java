package com.schedek.curso.ejb.view;

import com.schedek.curso.ejb.entities.Booking;
import com.schedek.curso.ejb.xmladapter.BigDecimalAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity
@Table(name = "v_booking_stats")
@Cacheable(false)
public class ViewBookingStats implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@OneToOne
	private Booking booking;
	@Column(precision = 9, scale = 2, nullable = false)
	private BigDecimal laundry;
	@Column(precision = 9, scale = 2, nullable = false)
	private BigDecimal cleaning;
	@Column(precision = 9, scale = 2, nullable = false)
	private BigDecimal depositTotal;
	@Column(precision = 9, scale = 2, nullable = false)
	private BigDecimal fee;
	@Column(precision = 9, scale = 2, nullable = false)
	private BigDecimal feevat;
	@Deprecated
	@Column(precision = 9, scale = 2, nullable = false)
	private BigDecimal laundry_all;
	@Deprecated
	@Column(precision = 9, scale = 2, nullable = false)
	private BigDecimal cleaning_all;
	@Column(precision = 9, scale = 2, nullable = false)
	private BigDecimal fee_all;
	@Deprecated
	private long laundry_all_count;
	@Deprecated
	private long cleaning_all_count;
	private long fee_all_count;
	@OneToOne
	private Booking next;
	@OneToOne
	private Booking prev;
	@Column(precision = 9, scale = 2)
	private BigDecimal transaction_items_sum;
	@Column(precision = 9)
	private BigDecimal transaction_booking_items_sum;
	@Column(precision = 9)
	private BigDecimal transaction_other_items_sum;
	@Column(precision = 9, scale = 2)
	private BigDecimal transaction_items_price;
	@Column(precision = 9, scale = 2)
	private BigDecimal transaction_booking_items_price;
	private Long transaction_items_count;
	private Long transaction_booking_items_count;
	private Long transaction_other_items_count;
    private Long notes_count;

	public ViewBookingStats() {
	}

	@XmlTransient
	public Booking getBooking() {
		return booking;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getLaundry() {
		return laundry;
	}

	@XmlTransient
	public Booking getNext() {
		return next;
	}

	public void setNext(Booking next) {
		this.next = next;
	}

	@XmlTransient
	public Booking getPrev() {
		return prev;
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getCleaning() {
		return cleaning;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public BigDecimal getFeevat() {
		return feevat;
	}

	public void setLaundry(BigDecimal laundry) {
		this.laundry = laundry;
	}

	public void setCleaning(BigDecimal cleaning) {
		this.cleaning = cleaning;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public void setFeevat(BigDecimal feevat) {
		this.feevat = feevat;
	}

	public BigDecimal getDepositTotal() {
		return depositTotal;
	}

	public void setDepositTotal(BigDecimal depositTotal) {
		this.depositTotal = depositTotal;
	}

	@Deprecated
	@XmlTransient
	public BigDecimal getLaundry_all() {
		return laundry_all;
	}

	@Deprecated
	@XmlTransient
	public BigDecimal getCleaning_all() {
		return cleaning_all;
	}

	@XmlTransient
	public BigDecimal getFee_all() {
		return fee_all;
	}

	@Deprecated
	@XmlTransient
	public long getLaundry_all_count() {
		return laundry_all_count;
	}

	@Deprecated
	@XmlTransient
	public long getCleaning_all_count() {
		return cleaning_all_count;
	}

	@XmlTransient
	public long getFee_all_count() {
		return fee_all_count;
	}

	public BigDecimal getTransaction_items_sum() {
		return transaction_items_sum;
	}

	public void setTransaction_items_sum(BigDecimal transaction_items_sum) {
		this.transaction_items_sum = transaction_items_sum;
	}

	public BigDecimal getTransaction_booking_items_sum() {
		return transaction_booking_items_sum;
	}

	public void setTransaction_booking_items_sum(BigDecimal transaction_booking_items_sum) {
		this.transaction_booking_items_sum = transaction_booking_items_sum;
	}

	public BigDecimal getTransaction_other_items_sum() {
		return transaction_other_items_sum;
	}

	public void setTransaction_other_items_sum(BigDecimal transaction_other_items_sum) {
		this.transaction_other_items_sum = transaction_other_items_sum;
	}

	public BigDecimal getTransaction_items_price() {
		return transaction_items_price;
	}

	public void setTransaction_items_price(BigDecimal transaction_items_price) {
		this.transaction_items_price = transaction_items_price;
	}

	public BigDecimal getTransaction_booking_items_price() {
		return transaction_booking_items_price;
	}

	public void setTransaction_booking_items_price(BigDecimal transaction_booking_items_price) {
		this.transaction_booking_items_price = transaction_booking_items_price;
	}

	public Long getTransaction_items_count() {
		return transaction_items_count;
	}

	public void setTransaction_items_count(Long transaction_items_count) {
		this.transaction_items_count = transaction_items_count;
	}

	public Long getTransaction_booking_items_count() {
		return transaction_booking_items_count;
	}

	public void setTransaction_booking_items_count(Long transaction_booking_items_count) {
		this.transaction_booking_items_count = transaction_booking_items_count;
	}

	public Long getTransaction_other_items_count() {
		return transaction_other_items_count;
	}

	public void setTransaction_other_items_count(Long transaction_other_items_count) {
		this.transaction_other_items_count = transaction_other_items_count;
	}

    public Long getNotes_count() {
        return notes_count;
    }

    public void setNotes_count(Long notes_count) {
        this.notes_count = notes_count;
    }

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 19 * hash + Objects.hashCode(this.booking);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ViewBookingStats other = (ViewBookingStats) obj;
		if (!Objects.equals(this.booking, other.booking)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ViewBookingStats{" + "booking=" + booking + '}';
	}

}
