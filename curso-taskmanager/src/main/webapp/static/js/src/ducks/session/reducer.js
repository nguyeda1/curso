import * as types from "es6!src/ducks/session/types";
import * as storage from "es6!src/helpers/localStorage";
import moment from "moment";
import * as R from "ramda";

import * as caseListTypes from "es6!src/ducks/case_list/types";

const initialState = {
  currentUser: undefined,
  isAuthenticated: false,
  savedPass: storage.getPassword(),
  error: undefined,
  loading: false
};

const reducer = (state = initialState, action) => {
  if (
    R.endsWith("ERROR", action.type) &&
    action.payload.response.status == 401
  ) {
    return initialState;
  }
  switch (action.type) {
    case types.WHOAMI_FULFILLED: {
      var user = action.payload;
      return Object.assign({}, state, {
        currentUser: user,
        isAuthenticated: user ? true : false
      });
    }

    case types.WHOAMI_ERROR: {
      var user = action.payload;
      return Object.assign({}, state, {
        currentUser: undefined,
        isAuthenticated: false
      });
    }
    case types.LOGIN_USER: {
      return Object.assign({}, state, {
        loading: true
      });
    }
    case types.LOGIN_USER_ERROR: {
      return Object.assign({}, state, {
        error: action.payload,
        loading: false
      });
    }
    case types.LOGIN_USER_FULFILLED: {
      var user = action.payload.user;

      return Object.assign({}, state, {
        currentUser: user,
        isAuthenticated: true,
        loading: false
      });
    }

    case types.LOGOUT_USER_FULFILLED: {
      return Object.assign({}, state, {
        currentUser: undefined,
        isAuthenticated: false
      });
    }

    case types.SAVE_PASS: {
      return { ...state, savedPass: storage.getPassword() };
    }
  }
  return state;
};

export default reducer;
