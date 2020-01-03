import { createSelector } from "reselect";
import { combineReducers } from "redux";
import {
  FETCH_CASE_DETAIL_ERROR,
  FETCH_CASE_DETAIL,
  FETCH_CASE_DETAIL_FULFILLED,
  FOLLOW_CASE_FULFILLED
} from "es6!src/ducks/case_detail/general/types";
import { tagListTypes } from "es6!src/ducks/tag_list/index";
import { caseDetailTypes } from "es6!src/ducks/case_detail/index";
import b64toBlob from "es6!src/helpers/b64toBlob";
import updateArray from "es6!src/helpers/updateArray";
import groupByProperty from "es6!src/helpers/groupByProperty";
import createReducerWithPhotos from "es6!src/helpers/createReducerWithPhotos";

const initialState = {
  cursoCase: {},
  tags: [],
  fetching: false,
  fetched: false,
  error: undefined
};
const root = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_CASE_DETAIL: {
      return Object.assign({}, state, {
        fetching: true
      });
    }
    case FETCH_CASE_DETAIL_ERROR: {
      return Object.assign({}, state, {
        error: action.payload
      });
    }
    case FOLLOW_CASE_FULFILLED: {
      return { ...state, cursoCase: action.payload };
    }
    case caseDetailTypes.FETCH_CASE_DETAIL_FULFILLED: {
      const { cursoCase, taskCount, tags } = action.payload;
      return Object.assign({}, state, {
        cursoCase,
        tags,
        taskCount,
        fetched: true,
        fetching: false
      });
    }

    case tagListTypes.EDIT_TAGS_FULFILLED: {
      return Object.assign({}, state, { tags: action.payload });
    }
  }
  return state;
};

export default combineReducers({
  root,
  photoPanel: createReducerWithPhotos("CASE")
});
