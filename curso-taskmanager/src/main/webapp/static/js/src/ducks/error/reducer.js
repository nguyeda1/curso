import * as types from "es6!src/ducks/error/types";
import * as R from "ramda";

const initialState = {
  error: undefined
};

const reducer = (state = initialState, action) => {
  if (R.endsWith("ERROR", action.type)) {
    return { ...state, error: action.payload };
  }
  switch (action.type) {
    case types.ERROR_DIALOG_CLOSE: {
      return initialState;
    }
  }
  return state;
};

export default reducer;
