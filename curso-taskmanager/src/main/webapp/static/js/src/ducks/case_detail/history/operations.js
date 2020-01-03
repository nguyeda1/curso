import { actionFulfilled, actionError, GET, API_URL } from "es6!src/helpers/ajax";

import * as actions from "es6!src/ducks/case_detail/history/actions";

export const fetchListByCaseId = (caseId, pageNumber, pageMax) => {
  return dispatch => {
    const caseQuery = "?caseId=" + caseId;
    const page = pageNumber < 0 ? "" : "&page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { caseId, page: pageNumber, max: pageMax };

    dispatch(actions.fetch(meta));

    GET(API_URL + "/activity/init" + caseQuery + page + max)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchFulfilled(error));
      });
  };
};

export const fetchNext = (caseId, pageNumber, pageMax) => {
  return dispatch => {
    const caseQuery = "?caseId=" + caseId;
    const page = pageNumber < 0 ? "" : "&page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { caseId, page: pageNumber, max: pageMax };

    dispatch(actions.fetchNext(meta));
    GET(API_URL + "/activity/init" + caseQuery + page + max)
      .then(response => {
        dispatch(actions.fetchNextFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchNextError(error));
      });
  };
};
