import createReducerWithAsyncList from "es6!src/helpers/createReducerWithAsyncList";
import types, { NAME } from "es6!src/ducks/activity_list/types";
import { combineReducers } from "redux";
import moment from "moment";
import { getCurrentUser } from "es6!src/helpers/localStorage";

const initialState = {
  lastRead: undefined,
  unread: 0
};
const reducer = (state = initialState, action) => {
  switch (action.type) {
    case types.FETCH_FULFILLED: {
      const { lastRead } = action.payload;
      return {
        ...state,
        lastRead
      };
    }

    case types.REFRESH_FULFILLED: {
      const { data, lastRead } = action.payload;
      return {
        ...state,
        unread: state.unread + data.length,
        lastRead
      };
    }
    case types.READ: {
      return { ...state, unread: 0 };
    }
  }
  return state;
};

export default combineReducers({
  fetchInfo: reducer,
  list: createReducerWithAsyncList(NAME)
});
