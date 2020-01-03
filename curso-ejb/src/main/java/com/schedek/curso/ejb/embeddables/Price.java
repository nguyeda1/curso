package com.schedek.curso.ejb.embeddables;

import com.schedek.curso.ejb.entities.Currency;
import com.schedek.curso.ejb.xmladapter.BigDecimalAdapter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Embeddable
@Access(AccessType.FIELD)
public class Price implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	@Column(columnDefinition = "numeric(38,2)")
	private BigDecimal price = BigDecimal.ZERO;
	private Currency currency;

	public Price() {
	}

	public Price(BigDecimal price, Currency currency) {
		this.price = price;
		this.currency = currency;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		if (currency != null) {
			this.currency = currency;
		}
	}

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	public BigDecimal getPrice() {
		if (price == null) {
			price = BigDecimal.ZERO;
		}
		return price;
	}

	public void setPrice(BigDecimal price) {
		if (price == null) {
			this.price = new BigDecimal(0);
		} else {
			this.price = price;
		}
	}

	@Override
	public String toString() {
		return String.format("%.4f ", price).replace(".", ",") + (currency != null ? currency : "");
	}

	public String priceToString() {
		return String.format("%.2f ", price).replace(".", ",") + (currency != null ? currency : "");
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.price);
		hash = 97 * hash + Objects.hashCode(this.currency);
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
		final Price other = (Price) obj;
		if (!Objects.equals(this.price, other.price)) {
			return false;
		}
		if (!Objects.equals(this.currency, other.currency)) {
			return false;
		}
		return true;
	}

	@Deprecated
	public Price dup() throws CloneNotSupportedException {
		return clone();
	}

	@Override
	public Price clone() throws CloneNotSupportedException {
		Price p = (Price) super.clone();
		p.setCurrency(currency);
		p.setPrice(price);
		return p;
	}

	public String getSerialized() {
		ObjectOutputStream oos = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			oos.close();
			return bos.toString();
		} catch (IOException ex) {
			Logger.getLogger(Price.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}

	public static Price unserialize(String s) {
		ObjectInputStream ois = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(s.getBytes());
			ois = new ObjectInputStream(bis);
			Object obj = ois.readObject();
			ois.close();
			if (obj instanceof Price) {
				return (Price) obj;
			}
		} catch (Exception ex) {
			Logger.getLogger(Price.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
