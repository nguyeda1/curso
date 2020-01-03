import {
  FETCH,
  FETCH_FULFILLED,
  FETCH_ERROR,
  FETCH_NEXT_FULFILLED,
  FETCH_NEXT_ERROR,
  FETCH_NEXT
} from "es6!src/ducks/case_detail/feedback/types";

export const fetch = meta => {
  return {
    type: FETCH,
    meta
  };
};

export const fetchFulfilled = response => {
  return { type: FETCH_FULFILLED, payload: response.data };
};

export const fetchError = error => {
  return { type: FETCH_ERROR, payload: error };
};

export const fetchNext = meta => {
  return {
    type: FETCH_NEXT,
    meta
  };
};

export const fetchNextFulfilled = response => {
  return { type: FETCH_NEXT_FULFILLED, payload: response.data };
};

export const fetchNextError = error => {
  return { type: FETCH_NEXT_ERROR, payload: error };
};
