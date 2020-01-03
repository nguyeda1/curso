import types from "es6!src/ducks/case_list/types";
import { combineReducers } from "redux";
import createReducerWithAsyncList from "es6!src/helpers/createReducerWithAsyncList";

const filteredReducers = {
  MY_CASES: createReducerWithAsyncList(types.MY_CASES.NAME),
  ALL: createReducerWithAsyncList(types.ALL.NAME),
  NEW: createReducerWithAsyncList(types.NEW.NAME),
  IN_PROGRESS: createReducerWithAsyncList(types.IN_PROGRESS.NAME),
  REVIEW: createReducerWithAsyncList(types.REVIEW.NAME),
  DONE: createReducerWithAsyncList(types.DONE.NAME)
};

export default combineReducers({
  ...filteredReducers
});
