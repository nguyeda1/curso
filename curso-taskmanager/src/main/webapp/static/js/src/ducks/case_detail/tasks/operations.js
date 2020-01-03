import { POST, GET, API_URL } from "es6!src/helpers/ajax";

import * as actions from "es6!src/ducks/case_detail/tasks/actions";

export const fetchListByCaseId = caseId => {
  return dispatch => {
    dispatch(actions.fetch());

    GET(API_URL + "/task/init/" + caseId)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const assign = id => {
  return dispatch => {
    dispatch(actions.update(id));
    return POST(API_URL + "/task/" + id + "/assign-toggle")
      .then(response => {
        dispatch(actions.updateFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.updateError(error));
      });
  };
};

export const finishTask = id => {
  return dispatch => {
    dispatch(actions.update(id));
    return POST(API_URL + "/task/change-status/" + id)
      .then(response => {
        dispatch(actions.updateFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.updateError(error));
      });
  };
};
