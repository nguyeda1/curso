import { GET, POST, API_URL } from "es6!src/helpers/ajax";

import * as actions from "es6!src/ducks/case_detail/comments/actions";

export const fetchListByCaseId = (id, pageNumber, pageMax) => {
  return dispatch => {
    const page = pageNumber < 0 ? "" : "?page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { id, page: pageNumber, max: pageMax };

    dispatch(actions.fetch(meta));
    GET(API_URL + "/comment/case/" + id + page + max)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const addItem = (id, data, okCallback) => {
  return dispatch => {
    dispatch(actions.add());
    POST(API_URL + "/comment/case/" + id, data)
      .then(response => {
        okCallback();
        dispatch(actions.addFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.addError(error));
      });
  };
};

export const fetchNext = (id, pageNumber, pageMax) => {
  return dispatch => {
    const page = pageNumber < 0 ? "" : "?page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { id, page: pageNumber, max: pageMax };

    dispatch(actions.fetchNext(meta));
    GET(API_URL + "/comment/case/" + id + page + max)
      .then(response => {
        dispatch(actions.fetchNextFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchNextError(error));
      });
  };
};
