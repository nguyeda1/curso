/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.web.beans.list;

import com.schedek.curso.ejb.facade.util.AdvancedFilter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.event.data.SortEvent;

/**
 *
 * @author Root
 */
public abstract class AbstractListState implements Serializable {

    private int page = 0;
    private int rows = 50;
    private String sortBy;
    private String sortFunction;
    private String sortOrder;
    private DataTable datatable;
    private Map<String, Object> filterState = new HashMap<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
    }

    public DataTable getDatatable() {
        return null;
    }

    public void setDatatable(DataTable datatable) {
        ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        if (datatable.getSortBy() == null && sortBy != null) {
            datatable.setValueExpression("sortBy", expressionFactory.createValueExpression(elContext, sortBy, Object.class));
            if (sortFunction != null) {
                datatable.setSortFunction(expressionFactory.createMethodExpression(elContext, sortFunction, Integer.TYPE, new Class[]{Object.class, Object.class}));
            }
            if (sortOrder != null) {
                datatable.setSortOrder(sortOrder);
            }
        }
        datatable.setFilters(filterState);
        this.datatable = datatable;
    }

    public void onPageChange(PageEvent event) {
        this.setPage(((DataTable) event.getSource()).getFirst());
    }

    public void onSortChange(SortEvent event) {
        sortBy = (event.getSortColumn().getValueExpression("sortBy").getExpressionString());
        sortFunction = event.getSortColumn().getSortFunction() != null ? (event.getSortColumn().getSortFunction().getExpressionString()) : null;
        sortOrder = (event.isAscending() ? "ASCENDING" : "DESCENDING");
    }

    public void onFilterChange(FilterEvent filterEvent) {
        //filterState not working with 5.0 (coming 1 step late), filterEvent.getData() not working with 5.1 (no docs)
        // probably would be fixed in 5.2
//		filterState = new HashMap<>(filterEvent.getFilters());
        //setFilteredValueInt(filterEvent.getData());
    }

    public FilterValue filterState(final String column) {
        return new FilterValue() {

            @Override
            public void setValue(Object o) {
                filterState.put(column, o);
            }

            @Override
            public Object getValue() {
				Object o = filterState.get(column);
				if(o instanceof AdvancedFilter){
					AdvancedFilter a=(AdvancedFilter) o;
					return a.getValue();
				}
                return o;
            }
        };
    }

    protected abstract void setFilteredValueInt(List filteredValue);

    public interface FilterValue {

        public void setValue(Object o);

        public Object getValue();
    }

}
