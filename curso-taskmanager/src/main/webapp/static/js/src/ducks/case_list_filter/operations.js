import * as actions from "es6!src/ducks/case_list_filter/actions";

export const filterCases = state => {
  return dispatch => dispatch(actions.filter(state));
};
