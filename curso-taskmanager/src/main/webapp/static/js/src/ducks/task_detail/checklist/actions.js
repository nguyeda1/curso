import {
  FETCH_TASK_CHECKLIST_ERROR,
  FETCH_TASK_CHECKLIST,
  FETCH_TASK_CHECKLIST_FULFILLED,
  UPDATE_SUBTASK,
  UPDATE_SUBTASK_ERROR,
  UPDATE_SUBTASK_FULFILLED
} from "es6!src/ducks/task_detail/checklist/types";
import { taskDetailTypes } from "es6!src/ducks/task_detail/index";
import { GET, API_URL } from "es6!src/helpers/ajax";

export const fetch = () => {
  return {
    type: FETCH_TASK_CHECKLIST
  };
};

export const fetchFulfilled = response => {
  return { type: FETCH_TASK_CHECKLIST_FULFILLED, payload: response.data };
};

export const fetchError = error => {
  return { type: FETCH_TASK_CHECKLIST_ERROR, payload: error };
};

export const update = data => {
  return {
    type: UPDATE_SUBTASK,
    payload: data
  };
};

export const updateFulfilled = response => {
  return { type: UPDATE_SUBTASK_FULFILLED, payload: response.data };
};

export const updateError = error => {
  return { type: UPDATE_SUBTASK_ERROR, payload: error };
};

export const finishTask = data => {
  return {
    type: taskDetailTypes.UPDATE_TASK_DETAIL,
    payload: data
  };
};

export const finishTaskFulfilled = response => {
  return {
    type: taskDetailTypes.UPDATE_TASK_DETAIL_FULFILLED,
    payload: response.data
  };
};

export const finishTaskError = error => {
  return { type: taskDetailTypes.UPDATE_TASK_DETAIL_ERROR, payload: error };
};
