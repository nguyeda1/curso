package com.schedek.curso.ejb.util;

import javax.persistence.Transient;

public class UnObj {

	@Transient
	protected long UNID = Math.round(Math.random()*1000000);

	protected UnObj() {
	}

	public long getUNID() {
		return UNID;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 47 * hash + (int) (this.UNID ^ (this.UNID >>> 32));
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final UnObj other = (UnObj) obj;
		return this.UNID == other.UNID;
	}

}
