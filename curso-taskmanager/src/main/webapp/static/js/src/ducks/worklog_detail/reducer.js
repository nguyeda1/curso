import { createSelector } from "reselect";
import { combineReducers } from "redux";
import {
  FETCH_WORKLOG_DETAIL_ERROR,
  FETCH_WORKLOG_DETAIL,
  FETCH_WORKLOG_DETAIL_FULFILLED
} from "es6!src/ducks/worklog_detail/types";
import { worklogDetailTypes } from "es6!src/ducks/worklog_detail/index";
import updateArray from "es6!src/helpers/updateArray";
import groupByProperty from "es6!src/helpers/groupByProperty";

const initialState = {
  worklog: {},
  fetching: false,
  fetched: false,
  error: undefined
};
const reducer = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_WORKLOG_DETAIL: {
      return Object.assign({}, state, {
        fetching: true
      });
    }
    case FETCH_WORKLOG_DETAIL_ERROR: {
      return Object.assign({}, state, {
        error: action.payload
      });
    }
    case FETCH_WORKLOG_DETAIL_FULFILLED: {
      return {
        ...state,
        fetched: true,
        fetching: false,
        worklog: action.payload
      };
    }
  }
  return state;
};

export default reducer;
