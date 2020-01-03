import { createSelector } from "reselect";
import { getCurrentUser } from "es6!src/helpers/localStorage";

export const assigneeSelector = state => state.taskDetailHeader.assignee;
export const disabledSelector = state => state.taskDetailHeader.disabled;
export const checklistDoneSelector = state =>
  state.taskDetailHeader.checklistDone;
