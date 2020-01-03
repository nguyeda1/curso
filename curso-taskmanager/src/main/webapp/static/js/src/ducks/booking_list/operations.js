import { actionFulfilled, actionError, GET, API_URL } from "es6!src/helpers/ajax";
import * as actions from "es6!src/ducks/booking_list/actions";

export const fetchListByListingId = (listingId, pageNumber, pageMax) => {
  return dispatch => {
    const page = pageNumber < 0 ? "" : "?page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { listingId, page: pageNumber, max: pageMax };

    dispatch(actions.fetch(meta));
    GET(API_URL + "/booking/by-listing/" + listingId + page + max)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const fetchNext = (listingId, pageNumber, pageMax) => {
  return dispatch => {
    const page = pageNumber < 0 ? "" : "?page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { listingId, page: pageNumber, max: pageMax };

    dispatch(actions.fetchNext(meta));
    GET(API_URL + "/booking/by-listing/" + listingId + page + max)
      .then(response => {
        dispatch(actions.fetchNextFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchNextError(error));
      });
  };
};

export const asyncSearch = (listingId, query) => {
  return dispatch => {
    const searchQuery = query ? "&query=" + query : "";
    const listingQuery = "?listingId=" + listingId;
    const meta = { query, listingId };
    dispatch(actions.search(meta));
    GET(API_URL + "/booking/search/" + listingQuery + searchQuery)
      .then(response => {
        dispatch(actions.searchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.searchError(error));
      });
  };
};

export const searchById = (listingId, id) => {
  return dispatch => {
    const meta = { id, listingId };
    const idQuery = "?id=" + id;
    const listingQuery = "?listingId=" + listingId;
    dispatch(actions.searchById(meta));
    GET(API_URL + "/booking" + listingQuery + idQuery)
      .then(response => {
        dispatch(actions.searchByIdFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.searchByIdError(error));
      });
  };
};
