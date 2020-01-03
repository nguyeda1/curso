import {
  FETCH_WORKLOG_CATEGORY_LIST,
  FETCH_WORKLOG_CATEGORY_LIST_FULFILLED,
  FETCH_WORKLOG_CATEGORY_LIST_ERROR
} from "es6!src/ducks/worklog_category_list/types";

import updateArray from "es6!src/helpers/updateArray";
import groupByProperty from "es6!src/helpers/groupByProperty";

const initialState = {
  categories: [],
  fetching: false,
  fetched: false,
  error: null
};
const reducer = function(state = initialState, action) {
  switch (action.type) {
    case FETCH_WORKLOG_CATEGORY_LIST_ERROR: {
      return Object.assign({}, state, {
        error: action.payload
      });
    }

    case FETCH_WORKLOG_CATEGORY_LIST: {
      return Object.assign({}, state, {
        fetching: true
      });
    }
    case FETCH_WORKLOG_CATEGORY_LIST_FULFILLED: {
      return Object.assign({}, state, {
        categories: action.payload,
        fetched: true,
        fetching: false
      });
    }
  }
  return state;
};
export default reducer;
