import { createSelector } from "reselect";

export const dataSelector = state => state.userList.data;
export const fetchedSelector = state => state.userList.fetched;
export const totalSelector = state => state.userList.total;
export const pageSelector = state => state.userList.page;
export const moreToLoadSelector = state => state.userList.moreToLoad;
