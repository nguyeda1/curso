import {
  actionFulfilled,
  actionError,
  GET,
  POST,
  API_URL
} from "es6!src/helpers/ajax";
import {
  FETCH_WORKLOG_CATEGORY_LIST,
  FETCH_WORKLOG_CATEGORY_LIST_FULFILLED,
  FETCH_WORKLOG_CATEGORY_LIST_ERROR
} from "es6!src/ducks/worklog_category_list/types";

export const fetch = () => {
  return {
    type: FETCH_WORKLOG_CATEGORY_LIST
  };
};

export const fetchFulfilled = response => {
  return actionFulfilled(FETCH_WORKLOG_CATEGORY_LIST_FULFILLED, response);
};

export const fetchError = error => {
  return actionError(FETCH_WORKLOG_CATEGORY_LIST_ERROR, error);
};
