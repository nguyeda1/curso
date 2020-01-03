import * as actions from "es6!src/ducks/case_edit/actions";

import { GET, POST, API_URL } from "es6!src/helpers/ajax";

export const fetchCleanEditForm = () => {
  return dispatch => dispatch(actions.fetchClean());
};

export const clearCaseEdit = () => {
  return dispatch => dispatch(actions.clear());
};

export const fetchCaseEdit = id => {
  return dispatch => {
    dispatch(actions.fetch());

    GET(API_URL + "/case/" + id)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const editCase = (data, okCallBack) => {
  return dispatch => {
    dispatch(actions.edit(data));

    POST(API_URL + "/case", data)
      .then(response => {
        dispatch(actions.editFulfilled(response));
        okCallBack(response.data);
      })
      .catch(error => {
        dispatch(actions.editError(error));
      });
  };
};
