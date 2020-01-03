import { POST_PHOTO, GET, POST, DELETE, API_URL } from "es6!src/helpers/ajax";

import * as actions from "es6!src/ducks/worklog_detail/actions";
import { worklogEditOperations } from "es6!src/ducks/worklog_edit/index";

export const fetchById = id => {
  return dispatch => {
    dispatch(actions.fetch());

    GET(API_URL + "/worklog/" + id)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};
