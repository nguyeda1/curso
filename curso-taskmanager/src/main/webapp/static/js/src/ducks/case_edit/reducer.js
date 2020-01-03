import { createSelector } from "reselect";

import {
  FETCH_CASE_EDIT_ERROR,
  FETCH_CASE_EDIT,
  FETCH_CASE_EDIT_FULFILLED,
  EDIT_CASE_DETAIL,
  EDIT_CASE_DETAIL_FULFILLED,
  EDIT_CASE_DETAIL_ERROR,
  FETCH_CLEAN_EDIT_FORM,
  CLEAR_CASE_EDIT
} from "es6!src/ducks/case_edit/types";
import { tagListTypes } from "es6!src/ducks/tag_list/index";

import updateArray from "es6!src/helpers/updateArray";
import groupByProperty from "es6!src/helpers/groupByProperty";

const initialState = {
  cursoCase: {},
  fetching: false,
  fetched: false,
  error: undefined
};
const reducer = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_CASE_EDIT: {
      return Object.assign({}, state, {
        fetching: true
      });
    }
    case FETCH_CASE_EDIT_FULFILLED: {
      return Object.assign({}, state, {
        cursoCase: action.payload.cursoCase,
        fetched: true,
        fetching: false
      });
    }
    case FETCH_CASE_EDIT_ERROR: {
      return Object.assign({}, state, {
        error: action.payload
      });
    }
    case CLEAR_CASE_EDIT: {
      return Object.assign({}, state, initialState);
    }
    case FETCH_CLEAN_EDIT_FORM: {
      return Object.assign({}, state, { cursoCase: {}, fetched: true });
    }
  }
  return state;
};

export default reducer;
