import { createSelector } from "reselect";

export const caseSelector = state => state.caseDetailGeneral.root.cursoCase;
export const caseTagsSelector = state => state.caseDetailGeneral.root.tags;
export const caseIdSelector = (state, ownProps) => ownProps.caseId;
export const fetchedSelector = state => state.caseDetailGeneral.root.fetched;
export const taskListSizeSelector = state =>
  state.caseDetailGeneral.root.taskCount;

export const photosSelector = state =>
  state.caseDetailGeneral.photoPanel.photos;
export const photosLoadingSelector = state =>
  state.caseDetailGeneral.photoPanel.loading;
