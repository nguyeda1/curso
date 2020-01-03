import {
  FETCH_CASE_TASKS_ERROR,
  FETCH_CASE_TASKS,
  UPDATE_CASE_TASK,
  UPDATE_CASE_TASK_ERROR
} from "es6!src/ducks/case_detail/tasks/types";
import { caseDetailTypes } from "es6!src/ducks/case_detail/index";

export const fetch = () => {
  return {
    type: FETCH_CASE_TASKS
  };
};

export const fetchFulfilled = response => {
  return {
    type: caseDetailTypes.FETCH_CASE_TASKS_FULFILLED,
    payload: response.data
  };
};

export const fetchError = error => {
  return {
    type: FETCH_CASE_TASKS_ERROR,
    payload: error
  };
};

export const update = response => {
  return {
    type: UPDATE_CASE_TASK
  };
};

export const updateFulfilled = response => {
  return {
    type: caseDetailTypes.UPDATE_CASE_TASK_FULFILLED,
    payload: response.data
  };
};

export const updateError = error => {
  return {
    type: UPDATE_CASE_TASK_ERROR,
    payload: error
  };
};
