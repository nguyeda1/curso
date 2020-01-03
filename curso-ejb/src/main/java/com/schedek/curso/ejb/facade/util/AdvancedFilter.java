package com.schedek.curso.ejb.facade.util;

import java.io.Serializable;
import java.util.Date;

public class AdvancedFilter implements Serializable{

	private MatchMode matchMode;
	private Object value;

	public AdvancedFilter(MatchMode matchMode, Object value) {
		this.matchMode = matchMode;
		this.value = value;
	}

	public MatchMode getMatchMode() {
		return matchMode;
	}

	public Object getValue() {
		return value;
	}

	public Object[] getQueryParts() {
		Class rc = value.getClass();
		if (rc.isArray() )
                    return  (Object[]) value;
                else {
                    return new Object[]{value};
                }

	}

}
