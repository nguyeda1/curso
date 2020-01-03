import { TASK_DETAIL_TAB_SWITCH } from "es6!src/ducks/task_detail/tab_panel/types";
import { actionFulfilled, actionError, GET, API_URL } from "es6!src/helpers/ajax";

export const switchTab = value => {
  return { type: TASK_DETAIL_TAB_SWITCH, payload: value };
};
