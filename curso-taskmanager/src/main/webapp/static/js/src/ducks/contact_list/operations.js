import * as actions from "es6!src/ducks/contact_list/actions";

import { GET, API_URL } from "es6!src/helpers/ajax";

export const fetchContacts = () => {
  return dispatch => {
    dispatch(actions.fetch());
    GET(API_URL + "/contact/init")
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};
