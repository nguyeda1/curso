import types from "es6!src/ducks/task_list/types";
import getAsyncListActions from "es6!src/helpers/getAsyncListActions";

const switchTab = tab => {
  return { type: types.SWITCH_TAB, payload: tab };
};

export default { ...getAsyncListActions(types), switchTab };
