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
import { actionFulfilled, actionError, GET, API_URL } from "es6!src/helpers/ajax";

export const fetch = () => {
  return {
    type: FETCH_CASE_EDIT
  };
};

export const fetchFulfilled = response => {
  return actionFulfilled(FETCH_CASE_EDIT_FULFILLED, response);
};

export const fetchError = error => {
  return actionError(FETCH_CASE_EDIT_ERROR, error);
};

export const edit = () => {
  return {
    type: EDIT_CASE_DETAIL
  };
};

export const editFulfilled = response => {
  return actionFulfilled(EDIT_CASE_DETAIL_FULFILLED, response);
};

export const editError = error => {
  return actionError(EDIT_CASE_DETAIL_ERROR, error);
};

export const clear = () => {
  return {
    type: CLEAR_CASE_EDIT
  };
};

export const fetchClean = () => {
  return {
    type: FETCH_CLEAN_EDIT_FORM
  };
};
