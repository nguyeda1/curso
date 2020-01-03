import { getJson, setJson } from "es6!src/helpers/jsonParser";

export const CURRENT_USER = "currentUser";
export const PASSWORD = "password";

export const getCurrentUser = () => {
  return getJson(localStorage.getItem(CURRENT_USER));
};

export const setCurrentUser = user => {
  localStorage.setItem(CURRENT_USER, setJson(user));
};

export const unsetCurrentUser = () => {
  localStorage.removeItem(CURRENT_USER);
};

export const getPassword = () => {
  return getJson(localStorage.getItem(PASSWORD));
};

export const setPassword = password => {
  localStorage.setItem(PASSWORD, setJson(password));
};

export const unsetPassword = () => {
  localStorage.removeItem(PASSWORD);
};
