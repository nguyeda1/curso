import * as types from "es6!src/ducks/worklog_edit/types";

import updateArray from "es6!src/helpers/updateArray";
import {
  pushUniqueElements,
  insertUniqueElements
} from "es6!src/helpers/addUniqueElementToArray";

const initialState = {
  categoryList: [],
  vehicleList: [],
  typeList: [],
  isProject: false,
  processing: false,
  processed: false,
  error: undefined
};
const reducer = (state = initialState, action) => {
  switch (action.type) {
    case types.SWITCH_WORKLOG_TYPE: {
      return { ...state, isProject: action.payload };
    }

    case types.FETCH_WORKLOG_CATEGORIES: {
      return Object.assign({}, state, {
        processing: true
      });
    }
    case types.FETCH_WORKLOG_CATEGORIES_FULFILLED: {
      return Object.assign({}, state, {
        processed: true,
        processing: false,
        categoryList: action.payload.categories,
        vehicleList: action.payload.vehicles,
        typeList: action.payload.types
      });
    }
    case types.FETCH_WORKLOG_CATEGORIES_ERROR: {
      return Object.assign({}, state, {
        processing: false,
        processed: true,
        error: action.payload
      });
    }
    case types.EDIT_WORKLOG: {
      return Object.assign({}, state, {
        processing: true
      });
    }
    case types.EDIT_WORKLOG_FULFILLED: {
      return Object.assign({}, state, {
        processed: true,
        processing: false
      });
    }
    case types.EDIT_WORKLOG_ERROR: {
      return Object.assign({}, state, {
        processing: false,
        processed: true,
        error: action.payload
      });
    }
  }
  return state;
};

export default reducer;
