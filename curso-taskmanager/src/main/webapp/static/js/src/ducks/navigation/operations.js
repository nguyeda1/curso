import * as actions from "es6!src/ducks/navigation/actions";

import { GET, POST, API_URL } from "es6!src/helpers/ajax";

export const redirect = path => {
  window.location.hash = "#" + path;
  return dispatch => dispatch(actions.redirect(path));
};

export const internalAppRedirect = path => {
  window.open(`/${path}`, "_blank");
  return dispatch => dispatch(actions.redirect(path));
};

export const notificationRedirect = activity => {
  var path = "";
  if (activity.cursoCase) {
    path = "/case/view/" + activity.cursoCase.id;
  } else if (activity.task) {
    path = "/task/view/" + activity.task.id;
  }
  window.location.hash = "#" + path;
  return dispatch => dispatch(actions.redirect(path));
};
