import {
  actionFulfilled,
  actionError,
  GET,
  POST,
  API_URL
} from "es6!src/helpers/ajax";
import {
  FETCH_TAGS,
  FETCH_TAGS_FULFILLED,
  FETCH_TAGS_ERROR,
  EDIT_TAGS,
  EDIT_TAGS_FULFILLED,
  EDIT_TAGS_ERROR
} from "es6!src/ducks/tag_list/types";

export const fetch = () => {
  return {
    type: FETCH_TAGS
  };
};

export const fetchFulfilled = response => {
  return actionFulfilled(FETCH_TAGS_FULFILLED, response);
};

export const fetchError = error => {
  return actionError(FETCH_TAGS_ERROR, error);
};

export const edit = () => {
  return {
    type: EDIT_TAGS
  };
};

export const editFulfilled = response => {
  return { type: EDIT_TAGS_FULFILLED, payload: response.data };
};

export const editError = error => {
  return actionError(EDIT_TAGS_ERROR, error);
};
