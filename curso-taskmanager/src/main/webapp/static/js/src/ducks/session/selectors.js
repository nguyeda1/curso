import { getCurrentUser } from "es6!src/helpers/localStorage";
import { createSelector } from "reselect";

export const errorSelector = state => state.session.error;
export const currentUsernameSelector = () =>
  getCurrentUser() ? getCurrentUser().username : "";

export const currentUserSelector = state => state.session.currentUser;

export const isAuthenticatedSelector = createSelector(
  currentUserSelector,
  currentUser => (currentUser ? true : false)
);

export const savedPasswordSelector = state => state.session.savedPass;

export const isPasswordSavedSelector = createSelector(
  savedPasswordSelector,
  savedPass => (savedPass ? true : false)
);

export const isLoadingSelector = state => state.session.loading;
