import * as actions from "es6!src/ducks/task_detail/header/actions";
import { GET, POST, API_URL } from "es6!src/helpers/ajax";

export const assignToMe = id => {
  return dispatch => {
    dispatch(actions.assign(id));
    return POST(API_URL + "/task/" + id + "/assign-toggle")
      .then(response => {
        dispatch(actions.assignFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.assignError(error));
      });
  };
};
