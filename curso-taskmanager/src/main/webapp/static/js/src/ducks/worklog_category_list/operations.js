import * as actions from "es6!src/ducks/worklog_category_list/actions";

import {
  actionFulfilled,
  actionError,
  GET,
  POST,
  API_URL
} from "es6!src/helpers/ajax";

export const fetchList = isProject => {
  return dispatch => {
    dispatch(actions.fetch());
    GET(API_URL + "/worklog/categories?project=" + isProject)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};
