import {
  FETCH_TASK_CHECKLIST_ERROR,
  FETCH_TASK_CHECKLIST,
  FETCH_TASK_CHECKLIST_FULFILLED,
  UPDATE_SUBTASK_FULFILLED
} from "es6!src/ducks/task_detail/checklist/types";
import { taskDetailTypes } from "es6!src/ducks/task_detail/index";
import updateArray from "es6!src/helpers/updateArray";
import { getCurrentUser } from "es6!src/helpers/localStorage";

const initialState = {
  checklist: [],
  isMine: false,
  taskFinished: false,
  fetching: false,
  fetched: false,
  error: undefined
};
const reducer = (state = initialState, action = {}) => {
  switch (action.type) {
    case FETCH_TASK_CHECKLIST: {
      return {
        ...state,
        fetching: true
      };
    }
    case FETCH_TASK_CHECKLIST_ERROR: {
      return {
        ...state,
        error: action.payload
      };
    }
    case FETCH_TASK_CHECKLIST_FULFILLED: {
      return {
        ...state,
        checklist: action.payload,
        fetched: true,
        fetching: false
      };
    }
    case UPDATE_SUBTASK_FULFILLED: {
      const checklist = updateArray(state.checklist, action.payload);

      return {
        ...state,
        checklist
      };
    }
    case taskDetailTypes.FETCH_TASK_DETAIL_FULFILLED: {
      const { task } = action.payload;
      const assignee = task.assignee ? task.assignee : {};
      const isMine = assignee.id
        ? getCurrentUser().id === assignee.id ? true : false
        : false;
      return {
        ...state,
        isMine,
        taskFinished: task.finished
      };
    }
    case taskDetailTypes.UPDATE_TASK_DETAIL_FULFILLED: {
      const assignee = action.payload.assignee ? action.payload.assignee : {};
      const isMine = assignee.id
        ? getCurrentUser().id === assignee.id ? true : false
        : false;
      return {
        ...state,
        isMine,
        taskFinished: action.payload.finished
      };
    }
  }
  return state;
};

export default reducer;
