import { createSelector } from "reselect";

export const notificationCountSelector = state =>
  state.activityList.fetchInfo.unread;

export const currentPathSelector = state => state.navigation.currentPath;
