import {
  actionFulfilled,
  actionError,
  GET,
  API_URL
} from "es6!src/helpers/ajax";
import * as actions from "es6!src/ducks/activity_list/actions";
import { navigationOperations } from "es6!src/ducks/navigation/index";

import moment from "moment";

export const tagAsRead = () => {
  return dispatch => {
    dispatch(actions.read());
  };
};

export const fetchNew = isFirst => {
  return dispatch => {
    dispatch(actions.refresh());
    const isReading = window.location.hash.startsWith("#/activity/list");
    const firstQuery = isFirst ? "?firstFetch=true" : "";
    const readQuery = isReading
      ? isFirst ? "&reading=true" : "?reading=true"
      : "";
    GET(API_URL + "/activity/refresh" + firstQuery + readQuery)
      .then(response => {
        dispatch(actions.refreshFulfilled(response));
        if (isReading) {
          dispatch(actions.read());
        }
        response.data.data.map(n => {
          const callback = () =>
            dispatch(navigationOperations.notificationRedirect(n));
          notify(n.log, "Notification", callback, "Open case");
        });
      })
      .catch(error => {
        dispatch(actions.refreshError(error));
      });
  };
};

export const fetchList = (pageNumber, pageMax) => {
  return dispatch => {
    const page = pageNumber < 0 ? "" : "?page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { page: pageNumber, max: pageMax };

    dispatch(actions.fetch(meta));

    GET(API_URL + "/activity/init" + page + max)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const fetchNext = (pageNumber, pageMax) => {
  return dispatch => {
    const page = pageNumber < 0 ? "" : "?page=" + pageNumber;
    const max = pageMax < 0 ? "" : "&max=" + pageMax;
    const meta = { page: pageNumber, max: pageMax };

    dispatch(actions.fetchNext(meta));
    GET(API_URL + "/activity/init" + page + max)
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
    GET(API_URL + "/activity/search" + searchQuery)
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
    dispatch(actions.searchById(meta));
    const idQuery = "?id=" + id;

    GET(API_URL + "/activity" + idQuery)
      .then(response => {
        dispatch(actions.searchByIdFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.searchByIdError(error));
      });
  };
};
