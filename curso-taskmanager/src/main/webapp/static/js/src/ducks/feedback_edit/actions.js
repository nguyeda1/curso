import {
  EDIT_FEEDBACK_ERROR,
  EDIT_FEEDBACK,
  EDIT_FEEDBACK_FULFILLED
} from "es6!src/ducks/feedback_edit/types";

export const edit = data => {
  return {
    type: EDIT_FEEDBACK,
    payload: data
  };
};

export const editFulfilled = response => {
  return { type: EDIT_FEEDBACK_FULFILLED, payload: response.data };
};

export const editError = error => {
  return { type: EDIT_FEEDBACK_ERROR, payload: error };
};
