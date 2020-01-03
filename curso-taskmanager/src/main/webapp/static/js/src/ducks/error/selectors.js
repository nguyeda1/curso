import { getCurrentUser } from "es6!src/helpers/localStorage";
import { createSelector } from "reselect";

export const errorSelector = state => state.error.error;

export const errorCodeSelector = createSelector(
  errorSelector,
  error => (error ? error.response.status : null)
);

export const errorTextSelector = createSelector(
  errorSelector,
  error => (error ? error.response.statusText : null)
);
