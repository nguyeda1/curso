import {
  FETCH_TAGS,
  FETCH_TAGS_FULFILLED,
  FETCH_TAGS_ERROR
} from "es6!src/ducks/tag_list/types";

import updateArray from "es6!src/helpers/updateArray";
import groupByProperty from "es6!src/helpers/groupByProperty";

const initialState = {
  tags: [],
  fetching: false,
  fetched: false,
  error: null
};
const reducer = function(state = initialState, action) {
  switch (action.type) {
    case FETCH_TAGS_ERROR: {
      return Object.assign({}, state, {
        error: action.payload
      });
    }

    case FETCH_TAGS: {
      return Object.assign({}, state, {
        fetching: true
      });
    }
    case FETCH_TAGS_FULFILLED: {
      return Object.assign({}, state, {
        tags: action.payload,
        fetched: true,
        fetching: false
      });
    }
  }
  return state;
};
export default reducer;
