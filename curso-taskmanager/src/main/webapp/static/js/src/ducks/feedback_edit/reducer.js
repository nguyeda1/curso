import {
  EDIT_FEEDBACK_ERROR,
  EDIT_FEEDBACK,
  EDIT_FEEDBACK_FULFILLED
} from "es6!src/ducks/feedback_edit/types";

import updateArray from "es6!src/helpers/updateArray";

const initialState = {
  processing: false,
  processed: false,
  error: undefined
};
const reducer = (state = initialState, action) => {
  switch (action.type) {
    case EDIT_FEEDBACK: {
      return Object.assign({}, state, {
        processing: true
      });
    }
    case EDIT_FEEDBACK_FULFILLED: {
      return Object.assign({}, state, {
        processed: true,
        processing: false
      });
    }
    case EDIT_FEEDBACK_ERROR: {
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
