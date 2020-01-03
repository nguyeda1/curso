import * as actions from "es6!src/ducks/worklog_edit/actions";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import moment from "moment";
import { GET, POST, API_URL } from "es6!src/helpers/ajax";

export const switchWorklogType = isProject => {
  return dispatch => {
    dispatch(actions.switchWorklogType(isProject));
    fetchCategories(isProject)(dispatch);
  };
};

export const throwEditableError = editable => {
  return dispatch => {
    if (!editable) {
      dispatch(
        actions.throwEditableError({
          response: { status: 403, statusText: "Worklog no longer editable" }
        })
      );
    }
  };
};

export const fetchCategories = isProject => {
  return dispatch => {
    dispatch(actions.fetchCategories(isProject));
    GET(API_URL + "/worklog/categories?project=" + isProject)
      .then(response => {
        dispatch(actions.fetchCategoriesFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchCategoriesError(error));
      });
  };
};

export const editWorklog = (id, data) => {
  return dispatch => {
    data.from = moment(data.from, "HH:mm");
    data.to = moment(data.to, "HH:mm");
    dispatch(actions.edit(data));
    const idQuery = id ? "?id=" + id : "";
    POST(API_URL + "/worklog" + idQuery, data)
      .then(response => {
        dispatch(actions.editFulfilled(response));
        navigationOperations.redirect("/worklog/view/" + response.data.id)(
          dispatch
        );
      })
      .catch(error => {
        dispatch(actions.editError(error));
      });
  };
};
