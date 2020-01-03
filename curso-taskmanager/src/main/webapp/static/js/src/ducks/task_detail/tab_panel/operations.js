import * as actions from "es6!src/ducks/task_detail/tab_panel/actions";

export const switchTab = tab => {
  return dispatch => dispatch(actions.switchTab(tab));
};
