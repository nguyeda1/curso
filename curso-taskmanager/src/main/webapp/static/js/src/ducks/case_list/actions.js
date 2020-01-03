import types from "es6!src/ducks/case_list/types";
import getAsyncListActions from "es6!src/helpers/getAsyncListActions";

export default filter => getAsyncListActions(types[filter]);
