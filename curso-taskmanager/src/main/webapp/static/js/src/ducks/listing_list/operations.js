import * as actions from "es6!src/ducks/listing_list/actions";

import { GET, API_URL } from "es6!src/helpers/ajax";

export const fetchListings = (pageNumber, pageMax) => {
  return dispatch => {
    const page = pageNumber < 0 ? "" : "?page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { page: pageNumber, max: pageMax };

    dispatch(actions.fetch(meta));
    GET(API_URL + "/listing/init" + page + max)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const fetchNext = (pageNumber, pageMax) => {
  return dispatch => {
    const page = pageNumber < 0 ? "" : "?page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { page: pageNumber, max: pageMax };

    dispatch(actions.fetchNext(meta));
    GET(API_URL + "/listing/init" + page + max)
      .then(response => {
        dispatch(actions.fetchNextFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchNextError(error));
      });
  };
};

export const asyncSearch = query => {
  return dispatch => {
    const searchQuery = query ? "?query=" + query : "";
    const meta = { query };
    dispatch(actions.search(meta));
    GET(API_URL + "/listing/search" + searchQuery)
      .then(response => {
        dispatch(actions.searchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.searchError(error));
      });
  };
};

export const searchById = id => {
  return dispatch => {
    const meta = { id };
    dispatch(actions.searchById(meta));
    GET(API_URL + "/listing?id=" + id)
      .then(response => {
        dispatch(actions.searchByIdFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.searchByIdError(error));
      });
  };
};
