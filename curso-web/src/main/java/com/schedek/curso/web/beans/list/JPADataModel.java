/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.web.beans.list;

/**
 *
 * @author Root
 */
import com.schedek.curso.ejb.facade.AbstractFacade;
import com.schedek.curso.ejb.facade.util.AdvancedFilter;
import com.schedek.curso.ejb.facade.util.MatchMode;
import com.schedek.curso.ejb.facade.util.QueryBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ValueExpression;
import javax.faces.component.StateHelper;
import javax.faces.component.TransientStateHelper;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Dummy implementation of LazyDataModel that uses a list to mimic a real
 * datasource like a database.
 *
 * @param <T>
 */
public class JPADataModel<T> extends LazyDataModel<T> {

	private AbstractFacade<T> facade;
	private boolean loaded = false;

	public JPADataModel(AbstractFacade<T> facade) {
		this.facade = facade;
	}

	protected QueryBuilder getQueryBuilder() {
		return facade.getDefaultQueryBuilder();
	}

	protected Object getID(T e) {
		return facade.getID(e);
	}

	protected T getT() {
		try {
			return (T) facade.getEntityClass().newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			Logger.getLogger(JPADataModel.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public T getRowData(String rowKey) {
		Class c = facade.getIDClass();
		Object key;
		if (Long.class.equals(c)) {
			key = Long.parseLong(rowKey);
		} else if (String.class.equals(c)) {
			key = rowKey;
		} else {
			throw new UnsupportedOperationException("unsupported primary key type");
		}
		return facade.find(key);
	}

	@Override
	public Object getRowKey(T car) {
		return getID(car);
	}

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

		applyAdvancedFilter(filters);

		Boolean asc = null;
		if (SortOrder.ASCENDING.equals(sortOrder)) {
			asc = true;
		}
		if (SortOrder.DESCENDING.equals(sortOrder)) {
			asc = false;
		}
		QueryBuilder<T> qb = getQueryBuilder();
		this.setRowCount(qb.count(filters));
		loaded = true;
		return qb.load(first, pageSize, sortField, asc, filters);
	}

	@Override
	public int getRowCount() {
		if (!loaded) {
			load(0, 1, null, SortOrder.UNSORTED, null);
		}
		return super.getRowCount(); //To change body of generated methods, choose Tools | Templates.
	}

	private void applyAdvancedFilter(Map<String, Object> filters) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		UIComponent comp = UIComponent.getCurrentComponent(ctx), c = comp;
		if (filters == null) {
			return;
		}
		if (c instanceof DataTable) {
			DataTable dt = (DataTable) c;
			if (dt.getColumns() != null) {
				for (UIColumn ucc : dt.getColumns()) {
					if (ucc == null || !(ucc instanceof Column)) {
						continue;
					}
					Column cc = (Column) ucc;
					ValueExpression ve = cc.getValueExpression("filterBy");
					if (ve == null) {
						continue;
					}

					String s = ve.getExpressionString();
					if (!s.contains(".") || !s.endsWith("}")) {
						continue;
					}
					s = s.substring(s.indexOf(".") + 1, s.length() - 1);
					Object f = filters.get(s);
					if (f != null && !(f instanceof AdvancedFilter)) {
						Column col = (Column) cc;
						filters.put(s, new AdvancedFilter(
								MatchMode.fromString(col.getFilterMatchMode()), f));
					}

				}
			}
		}

	}
}
