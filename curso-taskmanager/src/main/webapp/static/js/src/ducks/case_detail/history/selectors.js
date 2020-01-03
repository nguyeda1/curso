import { createSelector } from "reselect";

export const dataSelector = state => state.caseDetailHistory.data;
export const fetchedSelector = state => state.caseDetailHistory.fetched;
export const totalSelector = state => state.caseDetailHistory.total;
export const pageSelector = state => state.caseDetailHistory.page;

export const moreToLoadSelector = state => state.caseDetailHistory.moreToLoad;
