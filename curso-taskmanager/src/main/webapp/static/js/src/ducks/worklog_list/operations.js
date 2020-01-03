import {
  actionFulfilled,
  actionError,
  GET,
  API_URL
} from "es6!src/helpers/ajax";
import * as actions from "es6!src/ducks/worklog_list/actions";

export const fetch = (taskId, pageNumber, pageMax) => {
  return dispatch => {
    const id = "?id=" + taskId;
    const page = pageNumber < 0 ? "" : "&page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;

    const meta = { taskId, page: pageNumber, max: pageMax };

    dispatch(actions.fetch(meta));
    GET(API_URL + "/worklog/list" + id + page + max)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const fetchNext = (taskId, pageNumber, pageMax) => {
  return dispatch => {
    const id = "?id=" + taskId;
    const page = pageNumber < 0 ? "" : "&page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;

    const meta = { taskId, page: pageNumber, max: pageMax };

    dispatch(actions.fetchNext(meta));
    GET(API_URL + "/worklog/list" + id + page + max)
      .then(response => {
        dispatch(actions.fetchNextFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchNextError(error));
      });
  };
};
