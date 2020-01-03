import { createSelector } from "reselect";

export const dataSelector = state => state.caseDetailComments.data;
export const fetchedSelector = state => state.caseDetailComments.fetched;
export const totalSelector = state => state.caseDetailComments.total;
export const pageSelector = state => state.caseDetailComments.page;
export const moreToLoadSelector = state => state.caseDetailComments.moreToLoad;
export const caseIdSelector = (state, props) => {
  return props.caseId;
};
export const sortByDateSelector = createSelector(dataSelector, data => {
  return data.sort((a, b) => new Date(a.createdOn) - new Date(b.createdOn));
});
