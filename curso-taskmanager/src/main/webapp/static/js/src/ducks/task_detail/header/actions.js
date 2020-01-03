import { taskDetailTypes } from "es6!src/ducks/task_detail/index";

export const assign = id => {
  return { type: taskDetailTypes.UPDATE_TASK_DETAIL, payload: id };
};

export const assignFulfilled = response => {
  return {
    type: taskDetailTypes.UPDATE_TASK_DETAIL_FULFILLED,
    payload: response.data
  };
};

export const assignError = error => {
  return { type: taskDetailTypes.UPDATE_TASK_DETAIL_ERROR, payload: error };
};
