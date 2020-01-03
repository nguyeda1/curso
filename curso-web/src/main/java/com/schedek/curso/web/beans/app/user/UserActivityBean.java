package com.schedek.curso.web.beans.app.user;

import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.facade.UserFacade;
import com.schedek.curso.web.beans.sess.SessionBean;
import java.io.Serializable;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author tommo
 */
@Named(value = "userActivityBean")
@ViewScoped
public class UserActivityBean implements Serializable {
    
    @Inject
    SessionBean sb;
    @EJB
    UserFacade uf;
    
    private LineChartModel lineModel;
    private User filteredUser;
    private List<User> users;
    private Date date = new Date();
    
    public UserActivityBean() {
    }

    public LineChartModel getLineModel() {
        String maxDate = new String();
        lineModel = new LineChartModel();
        LineChartSeries series = new LineChartSeries();
        series.setLabel(filteredUser.getUsername()); 
        
        Calendar actual = Calendar.getInstance();
        Calendar db = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        lineModel.addSeries(series);        
        lineModel.setTitle("User activity");
        lineModel.getAxis(AxisType.Y).setLabel("Click count");
        
        DateAxis xAxis = new DateAxis("Time");
        xAxis.setTickAngle(-50);
        xAxis.setMax(maxDate);
        xAxis.setTickFormat("%H:%M");
        
        lineModel.getAxes().put(AxisType.X, xAxis);
        
        return lineModel;
    }

    public User getFilteredUser() {
        if (filteredUser == null) {
            filteredUser = sb.getUser();
        }
        return filteredUser;
    }

    public void setFilteredUser(User filteredUser) {
        this.filteredUser = filteredUser;
    }

    public List<User> getUsers() {
        if (users == null) {
            users = uf.qbFilterActive(null).loadAll();
        }
        return users;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
