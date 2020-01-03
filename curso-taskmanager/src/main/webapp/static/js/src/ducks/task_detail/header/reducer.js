import { taskDetailTypes } from "es6!src/ducks/task_detail/index";

const initialState = {
  assignee: {},
  checklistDone: false,
  disabled: false,
  selected: "General"
};
const reducer = (state = initialState, action = {}) => {
  switch (action.type) {
    case taskDetailTypes.FETCH_TASK_DETAIL_FULFILLED: {
      const { task } = action.payload;
      const assignee = task.assignee;
      const checklistDone = task.checklistDone;
      return {
        ...state,
        assignee,
        checklistDone,
        disabled: task.finished
      };
    }

    case taskDetailTypes.UPDATE_TASK_DETAIL_FULFILLED: {
      const assignee = action.payload.assignee ? action.payload.assignee : {};
      return { ...state, assignee, disabled: action.payload.finished };
    }
  }

  return state;
};

export default reducer;
