import * as actions from "es6!src/ducks/tag_list/actions";

import {
  actionFulfilled,
  actionError,
  GET,
  POST,
  API_URL
} from "es6!src/helpers/ajax";

export const fetchList = () => {
  return dispatch => {
    dispatch(actions.fetch());
    GET(API_URL + "/tag/init")
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const editCaseTags = (caseId, data, okCallback) => {
  return dispatch => {
    dispatch(actions.edit());

    POST(API_URL + "/tag/add/" + caseId, data)
      .then(response => {
        dispatch(actions.editFulfilled(response));
        okCallback();
      })
      .catch(error => {
        dispatch(actions.editFulfilled(error));
      });
  };
};
