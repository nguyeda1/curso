import getAsyncListTypes from "es6!src/helpers/getAsyncListTypes";

export const NAME = "MY_TASKS";

const SWITCH_TAB = "SWITCH_" + NAME + "_TAB";

export default { ...getAsyncListTypes(NAME), SWITCH_TAB };
