import * as actions from "es6!src/ducks/session/actions";
import { navigationOperations } from "es6!src/ducks/navigation/index";

import { GET, POST, API_URL } from "es6!src/helpers/ajax";
import * as storage from "es6!src/helpers/localStorage";

export const whoami = () => {
  return dispatch => {
    dispatch(actions.whoami());
    GET(API_URL + "/user/whoami")
      .then(response => {
        storage.setCurrentUser(response.data);
        dispatch(actions.whoamiFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.whoamiFulfilled(error));
      });
  };
};

export const login = (username, password, savePassword, failureCallback) => {
  return dispatch => {
    dispatch(actions.login(username));
    POST(API_URL + "/user/login", { username, password })
      .then(response => {
        const data = response.data;
        storage.setCurrentUser(data.user);
        dispatch(actions.loginFulfilled(response));
        if (savePassword) {
          storage.setPassword(password);
        } else {
          storage.unsetPassword();
        }
        dispatch(actions.savePass());
        navigationOperations.redirect("/case/list")(dispatch);
      })
      .catch(error => {
        dispatch(actions.loginError(error));
        failureCallback();
      });
  };
};

export const logout = () => {
  return dispatch => {
    dispatch(actions.logout());
    GET(API_URL + "/user/logout")
      .then(response => {
        dispatch(actions.logoutFulfilled());
        dispatch(navigationOperations.redirect("/login"));
      })
      .catch(error => {
        dispatch(actions.logoutError(error));
      });
  };
};