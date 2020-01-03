import { createSelector } from "reselect";
import moment from "moment";

export const fetchedSelector = state => state.worklogDetail.fetched;
export const worklogSelector = state => state.worklogDetail.worklog;
export const worklogIdSelector = (state, ownProps) => ownProps.worklogId;
export const isEditableSelector = createSelector(
  worklogSelector,
  worklog =>
    worklog
      ? moment()
          .subtract(1, "days")
          .isBefore(moment(worklog.date))
      : false
);
