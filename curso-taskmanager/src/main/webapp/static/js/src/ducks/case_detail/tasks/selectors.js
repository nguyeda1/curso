import { createSelector } from "reselect";
import groupByProperty from "es6!src/helpers/groupByProperty";

export const tasksSelector = state => state.caseDetailTaskList.tasks;
export const groupsSelector = state => state.caseDetailTaskList.groups;
export const fetchedSelector = state => state.caseDetailTaskList.fetched;

export const taskListSizeSelector = state =>
  state.caseDetailTaskList.tasks.length;

export const tasksTotalSelector = state => state.caseDetailTaskList.total;

export const groupByPrioritySelector = createSelector(
  tasksSelector,
  groupsSelector,
  (tasks, groups) => groupByProperty(tasks, groups, "priority")
);
