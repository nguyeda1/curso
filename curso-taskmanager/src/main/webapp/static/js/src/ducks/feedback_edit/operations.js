import * as actions from "es6!src/ducks/feedback_edit/actions";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import { caseDetailFeedbackOperations } from "es6!src/ducks/case_detail/feedback/index";

import { GET, POST, API_URL } from "es6!src/helpers/ajax";

export const editFeedback = (caseId, data, okCallback) => {
  return dispatch => {
    dispatch(actions.edit(data));

    POST(API_URL + "/feedback/" + caseId, data)
      .then(response => {
        dispatch(actions.editFulfilled(response));
        navigationOperations.redirect("/case/view/" + caseId);
      })
      .catch(error => {
        dispatch(actions.editError(error));
      });
  };
};
