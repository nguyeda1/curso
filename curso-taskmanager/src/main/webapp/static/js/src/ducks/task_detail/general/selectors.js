import { createSelector } from "reselect";

export const taskSelector = state => state.taskDetailGeneral.root.task;
export const taskIdSelector = (state, ownProps) => ownProps.taskId;
export const fetchedSelector = state => state.taskDetailGeneral.root.fetched;

export const photosSelector = state =>
  state.taskDetailGeneral.photoPanel.photos;
export const photosLoadingSelector = state =>
  state.taskDetailGeneral.photoPanel.loading;
