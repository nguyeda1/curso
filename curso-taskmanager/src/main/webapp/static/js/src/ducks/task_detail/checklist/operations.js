import { GET, POST, API_URL } from "es6!src/helpers/ajax";

import * as actions from "es6!src/ducks/task_detail/checklist/actions";

export const fetchListByTaskId = id => {
  return dispatch => {
    dispatch(actions.fetch());

    GET(API_URL + "/subtask/init/" + id)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const changeState = data => {
  return dispatch => {
    dispatch(actions.update(data));

    POST(API_URL + "/subtask", data)
      .then(response => {
        dispatch(actions.updateFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.updateError(error));
      });
  };
};

export const finishTask = (id, state, problemText) => {
  return dispatch => {
    dispatch(actions.finishTask(id));

    POST(
      API_URL +
        "/task/finish/" +
        id +
        "?state=" +
        state +
        "&problem=" +
        problemText
    )
      .then(response => {
        dispatch(actions.finishTaskFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.finishTaskError(error));
      });
  };
};
