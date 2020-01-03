import { FETCH_CASE_TASKS_ERROR, FETCH_CASE_TASKS } from "es6!src/ducks/case_detail/tasks/types";
import { tagListTypes } from "es6!src/ducks/tag_list/index";
import { caseDetailTypes } from "es6!src/ducks/case_detail/index";
import updateArray from "es6!src/helpers/updateArray";
import groupByProperty from "es6!src/helpers/groupByProperty";
import {
  pushUniqueElements,
  insertUniqueElements
} from "es6!src/helpers/addUniqueElementToArray";

const initialState = {
  tasks: [],
  groups: [],
  fetching: false,
  fetched: false,
  error: null
};
const reducer = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_CASE_TASKS: {
      return Object.assign({}, state, {
        fetching: true
      });
    }
    case FETCH_CASE_TASKS_ERROR: {
      return Object.assign({}, state, {
        error: action.payload
      });
    }
    case caseDetailTypes.FETCH_CASE_TASKS_FULFILLED: {
      return Object.assign({}, state, {
        tasks: action.payload,
        fetched: true,
        fetching: false
      });
    }
    case caseDetailTypes.UPDATE_CASE_TASK_FULFILLED: {
      const task = action.payload;
      const tasks = updateArray(state.tasks, task);
      return Object.assign({}, state, { tasks });
    }
  }
  return state;
};

export default reducer;
