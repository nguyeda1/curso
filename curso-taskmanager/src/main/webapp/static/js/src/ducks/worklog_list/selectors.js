import { createSelector } from "reselect";
import { sortWith, descend, ascend, prop } from "ramda";

export const dataSelector = state => {
  const dateTimeSort = sortWith([
    descend(prop("date")),
    descend(prop("from")),
    descend(prop("to"))
  ]);
  return dateTimeSort(state.worklogList.data);
};
export const fetchedSelector = state => state.worklogList.fetched;
export const totalSelector = state => state.worklogList.total;
export const pageSelector = state => state.worklogList.page;

export const moreToLoadSelector = state => state.worklogList.moreToLoad;

export const taskIdSelector = (state, props) => {
  return props.taskId;
};
