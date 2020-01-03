import types from "es6!src/ducks/activity_list/types";
import getAsyncListActions from "es6!src/helpers/getAsyncListActions";

const read = meta => {
  return { type: types.READ, meta };
};

export default {
  ...getAsyncListActions(types),
  read
};
