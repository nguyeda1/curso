import { createSelector } from "reselect";

export const dataSelector = state => state.bookingList.data;
export const fetchedSelector = state => state.bookingList.fetched;
export const totalSelector = state => state.bookingList.total;
export const pageSelector = state => state.bookingList.page;

export const moreToLoadSelector = state => state.bookingList.moreToLoad;

export const listingIdSelector = (state, props) => {
  return props.listingId;
};
