import { CASE_DETAIL_TAB_SWITCH } from "es6!src/ducks/case_detail/tab_panel/types";
import { actionFulfilled, actionError, GET, API_URL } from "es6!src/helpers/ajax";

export const switchTab = value => {
  return { type: CASE_DETAIL_TAB_SWITCH, payload: value };
};
