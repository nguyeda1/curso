import { createSelector } from "reselect";

export const dataSelector = state => state.caseDetailFeedback.data;
export const fetchedSelector = state => state.caseDetailFeedback.fetched;
export const totalSelector = state => state.caseDetailFeedback.total;
export const pageSelector = state => state.caseDetailFeedback.page;

export const moreToLoadSelector = state => state.caseDetailFeedback.moreToLoad;

export const isReviewSelector = state =>
  state.caseDetailGeneral.root.cursoCase.caseState === "REVIEW";
