import * as types from "es6!src/ducks/worklog_edit/types";

export const throwEditableError = error => {
  return { type: types.IS_EDITABLE_ERROR, payload: error };
};

export const switchWorklogType = data => {
  return {
    type: types.SWITCH_WORKLOG_TYPE,
    payload: data
  };
};

export const fetchCategories = data => {
  return {
    type: types.FETCH_WORKLOG_CATEGORIES,
    payload: data
  };
};

export const fetchCategoriesFulfilled = response => {
  return {
    type: types.FETCH_WORKLOG_CATEGORIES_FULFILLED,
    payload: response.data
  };
};

export const fetchCategoriesError = error => {
  return { type: types.FETCH_WORKLOG_CATEGORIES_ERROR, payload: error };
};

export const edit = data => {
  return {
    type: types.EDIT_WORKLOG,
    payload: data
  };
};

export const editFulfilled = response => {
  return { type: types.EDIT_WORKLOG_FULFILLED, payload: response.data };
};

export const editError = error => {
  return { type: types.EDIT_WORKLOG_ERROR, payload: error };
};
