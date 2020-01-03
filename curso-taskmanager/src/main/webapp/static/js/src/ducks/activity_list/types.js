import getAsyncListTypes from "es6!src/helpers/getAsyncListTypes";

export const NAME = "ACTIVITY_LIST";

const READ = "READ";

export default {
  ...getAsyncListTypes(NAME),
  READ
};
