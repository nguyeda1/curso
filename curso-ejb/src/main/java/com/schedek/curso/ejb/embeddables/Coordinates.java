package com.schedek.curso.ejb.embeddables;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Coordinates {

	private static final long serialVersionUID = 1L;
	@Column(columnDefinition = "numeric(38,12)")
	private BigDecimal latitude;
	@Column(columnDefinition = "numeric(38,12)")
	private BigDecimal longitude;

	public Coordinates() {
	}

	public Coordinates(BigDecimal latitude, BigDecimal longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 23 * hash + Objects.hashCode(this.latitude);
		hash = 23 * hash + Objects.hashCode(this.longitude);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Coordinates other = (Coordinates) obj;
		if (!Objects.equals(this.latitude, other.latitude)) {
			return false;
		}
		if (!Objects.equals(this.longitude, other.longitude)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Coordinates{" + "latitude=" + latitude + ", longitude=" + longitude + '}';
	}

}
