import * as actions from "es6!src/ducks/task_list/actions";

import { GET, POST, API_URL } from "es6!src/helpers/ajax";

export const fetchTasks = (finished, pageNumber, pageSize) => {
  return dispatch => {
    const finishedQuery = finished ? "?finished=true" : "?finished=false";
    const pageQuery = pageNumber ? "&page=" + pageNumber : "";
    const maxQuery = pageSize ? "&max=" + pageSize : "";
    const query = finishedQuery + pageQuery + maxQuery;
    dispatch(actions.fetch({ finished, pageNumber, pageSize }));
    GET(API_URL + "/task/init" + query)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const finishTask = (id, state, problemText) => {
  return dispatch => {
    dispatch(actions.update({ id, state, problemText }));
    return POST(
      API_URL +
        "/task/finish/" +
        id +
        "?state=" +
        state +
        "&problem=" +
        problemText
    )
      .then(response => {
        dispatch(actions.updateFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.updateError(error));
      });
  };
};

export const switchTab = tab => {
  return dispatch => {
    dispatch(actions.switchTab(tab));
  };
};

export const fetchNext = (pageNumber, pageMax) => {
  return dispatch => {
    const finished = "?finished=true";
    const page = pageNumber < 0 ? "" : "&page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { finished, page: pageNumber, max: pageMax };
    const query = finished + page + max;
    dispatch(actions.fetchNext(meta));
    GET(API_URL + "/task/init" + query)
      .then(response => {
        dispatch(actions.fetchNextFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchNextError(error));
      });
  };
};

export const asyncSearch = query => {
  return dispatch => {
    const searchQuery = query ? "?query=" + query : "";
    const meta = { query };
    dispatch(actions.search(meta));
    GET(API_URL + "/task/search" + searchQuery)
      .then(response => {
        dispatch(actions.searchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.searchError(error));
      });
  };
};

export const searchById = id => {
  return dispatch => {
    const meta = { id };
    const idQuery = "?id=" + id;
    dispatch(actions.searchById(meta));
    GET(API_URL + "/task" + idQuery)
      .then(response => {
        dispatch(actions.searchByIdFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.searchByIdError(error));
      });
  };
};
