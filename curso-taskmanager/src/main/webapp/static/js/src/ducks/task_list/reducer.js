import createReducerWithAsyncList from "es6!src/helpers/createReducerWithAsyncList";
import types, { NAME } from "es6!src/ducks/task_list/types";
import { combineReducers } from "redux";

const initialState = {
  currentTab: "IN_PROGRESS"
};
const tabReducer = (state = initialState, action) => {
  switch (action.type) {
    case types.SWITCH_TAB: {
      return { ...state, currentTab: action.payload };
    }
  }
  return state;
};

export default combineReducers({
  tab: tabReducer,
  list: createReducerWithAsyncList(NAME)
});
