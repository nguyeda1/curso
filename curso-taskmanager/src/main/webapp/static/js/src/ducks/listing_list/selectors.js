import { createSelector } from "reselect";

export const dataSelector = state => state.listingList.data;
export const fetchedSelector = state => state.listingList.fetched;
export const totalSelector = state => state.listingList.total;
export const pageSelector = state => state.listingList.page;
export const moreToLoadSelector = state => state.listingList.moreToLoad;
