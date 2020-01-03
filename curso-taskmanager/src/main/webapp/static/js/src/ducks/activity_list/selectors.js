import { createSelector } from "reselect";

export const dataSelector = state => state.activityList.list.data;
export const fetchedSelector = state => state.activityList.list.fetched;
export const totalSelector = state => state.activityList.list.total;
export const pageSelector = state => state.activityList.list.page;

export const lastReadSelector = state => state.activityList.fetchInfo.lastRead;

export const moreToLoadSelector = state => state.activityList.list.moreToLoad;
