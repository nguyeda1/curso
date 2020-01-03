import { taskDetailTypes } from "es6!src/ducks/task_detail/index";
import { TASK_DETAIL_TAB_SWITCH } from "es6!src/ducks/task_detail/tab_panel/types";

const initialState = {
  commentCount: 0,
  checklistCount: 0,
  selected: "General"
};
const reducer = (state = initialState, action = {}) => {
  switch (action.type) {
    case taskDetailTypes.FETCH_TASK_DETAIL_FULFILLED: {
      const { commentCount, checklistCount } = action.payload;
      return {
        ...state,
        commentCount,
        checklistCount
      };
    }
    case TASK_DETAIL_TAB_SWITCH: {
      return {
        ...state,
        selected: action.payload
      };
    }
  }

  return state;
};

export default reducer;
