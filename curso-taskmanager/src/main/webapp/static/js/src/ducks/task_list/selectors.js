import { createSelector } from "reselect";
import groupByProperty from "es6!src/helpers/groupByProperty";
import { sortWith, descend, ascend, prop } from "ramda";

export const dataSelector = state => state.taskList.list.data;
export const fetchedSelector = state => state.taskList.list.fetched;
export const totalSelector = state => state.taskList.list.total;
export const pageSelector = state => state.taskList.list.page;
export const moreToLoadSelector = state => state.taskList.list.moreToLoad;

export const groupsSelector = state => [];

export const tabSelector = state => state.taskList.tab.currentTab;

const planLocPrioSort = sortWith([
  ascend(prop("plannedOn")),
  ascend(prop("locality")),
  descend(prop("priorityOnScreen"))
]);

export const filteredTasksSelector = createSelector(
  dataSelector,
  tabSelector,
  (tasks, tab) =>
    planLocPrioSort(
      tasks.filter(t => (tab === "IN_PROGRESS" ? !t.finished : t.finished))
    )
);

export const taskListSizeSelector = createSelector(
  filteredTasksSelector,
  tabSelector,
  totalSelector,
  (tasks, tab, total) => (tab === "IN_PROGRESS" ? tasks.length : total)
);

export const groupByListingSelector = createSelector(
  filteredTasksSelector,
  groupsSelector,
  (tasks, groups) => {
    return groupByProperty(tasks, groups, "plannedOn");
  }
);
