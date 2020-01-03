import { taskDetailTypes } from "es6!src/ducks/task_detail/index";
import createReducerWithPhotos from "es6!src/helpers/createReducerWithPhotos";
import { combineReducers } from "redux";

const initialState = {
  task: {},
  fetching: false,
  fetched: false,
  error: undefined
};
const root = (state = initialState, action = {}) => {
  switch (action.type) {
    case taskDetailTypes.FETCH_TASK_DETAIL: {
      return {
        ...state,
        fetching: true
      };
    }
    case taskDetailTypes.FETCH_TASK_DETAIL_ERROR: {
      return {
        ...state,
        error: action.payload
      };
    }
    case taskDetailTypes.FETCH_TASK_DETAIL_FULFILLED: {
      const { task } = action.payload;
      return {
        ...state,
        task: task,
        fetched: true,
        fetching: false
      };
    }
    case taskDetailTypes.UPDATE_TASK_DETAIL_FULFILLED: {
      return {
        ...state,
        task: action.payload
      };
    }
  }
  return state;
};

export default combineReducers({
  root,
  photoPanel: createReducerWithPhotos("TASK")
});
