import { createSelector } from "reselect";

export const dataSelector = state => state.taskDetailComments.data;
export const fetchedSelector = state => state.taskDetailComments.fetched;
export const totalSelector = state => state.taskDetailComments.total;
export const pageSelector = state => state.taskDetailComments.page;
export const moreToLoadSelector = state => state.taskDetailComments.moreToLoad;
export const taskIdSelector = (state, props) => {
  return props.taskId;
};
export const sortByDateSelector = createSelector(dataSelector, data => {
  return data.sort((a, b) => new Date(a.createdOn) - new Date(b.createdOn));
});
