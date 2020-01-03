import { caseDetailTypes } from "es6!src/ducks/case_detail/index";
import { caseDetailTaskListTypes } from "es6!src/ducks/case_detail/tasks/index";
import { CASE_DETAIL_TAB_SWITCH } from "es6!src/ducks/case_detail/tab_panel/types";

const initialState = {
  taskCount: 0,
  selected: "Case"
};
const reducer = (state = initialState, action) => {
  switch (action.type) {
    case caseDetailTypes.FETCH_CASE_DETAIL_FULFILLED: {
      const { taskCount } = action.payload;
      return Object.assign({}, state, {
        taskCount
      });
    }
    case caseDetailTypes.FETCH_CASE_TASKS_FULFILLED: {
      const { tasks } = action.payload;
      return Object.assign({}, state, {
        taskCount: action.payload.length
      });
    }
    case CASE_DETAIL_TAB_SWITCH: {
      return Object.assign({}, state, {
        selected: action.payload
      });
    }
  }
  return state;
};

export default reducer;
