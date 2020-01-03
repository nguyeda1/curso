import * as types from "es6!src/ducks/session/types";

export const whoami = () => {
  return {
    type: types.WHOAMI
  };
};

export const whoamiFulfilled = response => {
  return { type: types.WHOAMI_FULFILLED, payload: response.data };
};

export const whoamiError = error => {
  return { type: types.WHOAMI_ERROR, payload: error };
};

export const login = () => {
  return {
    type: types.LOGIN_USER
  };
};

export const loginFulfilled = response => {
  return { type: types.LOGIN_USER_FULFILLED, payload: response.data };
};

export const loginError = error => {
  return { type: types.LOGIN_USER_ERROR, payload: error };
};

export const logout = () => {
  return {
    type: types.LOGOUT_USER
  };
};

export const logoutFulfilled = response => {
  return { type: types.LOGOUT_USER_FULFILLED };
};

export const logoutError = error => {
  return { type: types.LOGOUT_USER_ERROR, payload: error };
};

export const savePass = () => {
  return { type: types.SAVE_PASS };
};
