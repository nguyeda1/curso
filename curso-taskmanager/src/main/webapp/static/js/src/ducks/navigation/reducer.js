import { REDIRECT } from "es6!src/ducks/navigation/types";

const initialState = { currentPath: window.location.href.split("#")[1] };
const reducer = (state = initialState, action) => {
  switch (action.type) {
    case REDIRECT: {
      return Object.assign({}, state, {
        currentPath: action.payload
      });
    }
  }
  return state;
};

export default reducer;
