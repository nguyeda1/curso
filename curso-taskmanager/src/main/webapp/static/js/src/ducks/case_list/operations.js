import * as actions from "es6!src/ducks/case_list/actions";

import { GET, POST, API_URL } from "es6!src/helpers/ajax";

export const fetchCases = (filterValue, pageNumber, pageMax) => {
  return dispatch => {
    const page = pageNumber < 0 ? "" : "?page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const filter = "&filter=" + filterValue;

    const meta = { page: pageNumber, filter: filterValue, max: pageMax };

    dispatch(actions(filterValue).fetch(meta));
    GET(API_URL + "/case/init" + page + max + filter)
      .then(response => {
        dispatch(actions(filterValue).fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions(filterValue).fetchError(error));
      });
  };
};

export const fetchNext = (filterValue, pageNumber, pageMax) => {
  return dispatch => {
    const page = pageNumber < 0 ? "" : "?page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const filter = "&filter=" + filterValue;

    const meta = { page: pageNumber, filter: filterValue, max: pageMax };

    dispatch(actions(filterValue).fetchNext(meta));
    GET(API_URL + "/case/init" + page + max + filter)
      .then(response => {
        dispatch(actions(filterValue).fetchNextFulfilled(response, meta));
      })
      .catch(error => {
        dispatch(actions(filterValue).fetchNextError(error, meta));
      });
  };
};

export const asyncSearch = (filterValue, query) => {
  return dispatch => {
    const filter = "&filter=" + filterValue;
    const searchQuery = query ? "?query=" + query : "";
    const meta = { query, filterValue };
    dispatch(actions(filterValue).search(meta));
    GET(API_URL + "/case/search" + searchQuery + filter)
      .then(response => {
        dispatch(actions(filterValue).searchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions(filterValue).searchError(error));
      });
  };
};

export const searchById = (filterValue, id) => {
  return dispatch => {
    const filter = "&filter=" + filterValue;
    const meta = { id, filterValue };
    dispatch(actions(filterValue).searchById(meta));
    GET(API_URL + "/case?id=" + id + filter)
      .then(response => {
        dispatch(actions(filterValue).searchByIdFulfilled(response));
      })
      .catch(error => {
        dispatch(actions(filterValue).searchByIdError(error));
      });
  };
};
