import {
  FETCH_WORKLOG_DETAIL_ERROR,
  FETCH_WORKLOG_DETAIL,
  FETCH_WORKLOG_DETAIL_FULFILLED
} from "es6!src/ducks/worklog_detail/types";
import { worklogDetailTypes } from "es6!src/ducks/worklog_detail/index";
import {
  actionFulfilled,
  actionError,
  GET,
  API_URL
} from "es6!src/helpers/ajax";

export const fetch = () => {
  return {
    type: FETCH_WORKLOG_DETAIL
  };
};

export const fetchFulfilled = response => {
  return actionFulfilled(FETCH_WORKLOG_DETAIL_FULFILLED, response);
};

export const fetchError = error => {
  return actionError(FETCH_WORKLOG_DETAIL_ERROR, error);
};
