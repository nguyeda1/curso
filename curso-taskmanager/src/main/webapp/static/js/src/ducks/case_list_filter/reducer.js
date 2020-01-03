import { caseListTypes } from "es6!src/ducks/case_list/index";

const initialState = {
  tabs: []
};
const reducer = (state = initialState, action) => {
  switch (action.type) {
    case caseListTypes.FETCH_CASES_FULFILLED: {
      const { states } = action.payload;
      const tabs = ["ALL"].concat(states);
      return Object.assign({}, state, {
        tabs
      });
    }
  }
  return state;
};

export default reducer;
