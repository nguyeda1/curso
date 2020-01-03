import { actionFulfilled, actionError, GET, API_URL } from "es6!src/helpers/ajax";

import * as actions from "es6!src/ducks/case_detail/tab_panel/actions";

export const switchTab = tab => {
  return dispatch => dispatch(actions.switchTab(tab));
};
