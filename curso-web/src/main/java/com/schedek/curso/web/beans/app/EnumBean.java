package com.schedek.curso.web.beans.app;

import com.schedek.curso.ejb.enums.*;
import com.schedek.curso.ejb.facade.booking.EventQueryMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

@Named(value = "enum")
@ApplicationScoped
public class EnumBean {

    public EnumBean() {
    }

    public List<BookingState> getBookingStates() {
        return Arrays.asList(BookingState.values());
    }

    public List<CaseFeedbackType> getCaseFeedbackType() {
        return Arrays.asList(CaseFeedbackType.values());
    }

    public List<EventQueryMode> getBookingEventQueryMode() {
        return Arrays.asList(EventQueryMode.values());
    }

    public List<BookingType> getBookingTypes() {
        return Arrays.asList(BookingType.values());
    }

    public List<BookingType> getBookingTypesPrice() {
        return Arrays.asList(new BookingType[]{BookingType.AIRBNB, BookingType.BOOKING});
    }

    public List<CaseState> getCaseState() {
        return Arrays.asList(CaseState.values());
    }

    public List<TaskState> getTaskState() {
        return Arrays.asList(TaskState.values());
    }

    public List<TaskFinishState> getTaskFinishState() {
        return Arrays.asList(TaskFinishState.values());
    }

    public List<CasePriority> getCasePriority() {
        return Arrays.asList(CasePriority.values());
    }

    public List<CaseOption> getCaseOption() {
        return Arrays.asList(CaseOption.values());
    }

    public List<CaseRole> getCaseRole() {
        return Arrays.asList(CaseRole.values());
    }

    public List<TaskPriority> getTaskPriority() {
        return Arrays.asList(TaskPriority.values());
    }

    public List<SubtaskState> getSubtaskState() {
        return Arrays.asList(SubtaskState.values());
    }

}
