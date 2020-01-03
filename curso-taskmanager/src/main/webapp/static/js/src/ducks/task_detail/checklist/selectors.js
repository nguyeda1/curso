import { createSelector } from "reselect";

export const checklistSelector = state => state.taskDetailChecklist.checklist;
export const fetchedSelector = state => state.taskDetailChecklist.fetched;
export const taskFinishedSelector = state =>
  state.taskDetailChecklist.taskFinished;

export const isMineSelector = state => state.taskDetailChecklist.isMine;

export const noneUnbegunSelector = createSelector(
  checklistSelector,
  checklist => {
    var res = true;
    checklist.forEach(s => {
      if (s.state === "UNBEGUN") {
        res = false;
      }
    });
    return res;
  }
);
