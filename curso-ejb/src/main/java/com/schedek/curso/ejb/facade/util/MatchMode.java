package com.schedek.curso.ejb.facade.util;

import java.util.Objects;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public enum MatchMode {

	STARTS_WITH_MATCH_MODE("startsWith"),
	ENDS_WITH_MATCH_MODE("endsWith"),
	CONTAINS_MATCH_MODE("contains"),
	EXACT_MATCH_MODE("exact"),
	LESS_THAN_MODE("lt"),
	LESS_THAN_EQUALS_MODE("lte"),
	GREATER_THAN_MODE("gt"),
	GREATER_THAN_EQUALS_MODE("gte"),
	EQUALS_MODE("equals"),
	IN_MODE("in"),
	GLOBAL_MODE("global");
	private String name;

	private MatchMode(String name) {
		Objects.requireNonNull(name);
		this.name = name;
	}

	public static MatchMode fromString(String name) {
		for (MatchMode v : values()) {
			if (v.name.equals(name)) {
				return v;
			}
		}
		return null;
	}
	

}
