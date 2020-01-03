import {
  FETCH_CONTACTS,
  FETCH_CONTACTS_FULFILLED,
  FETCH_CONTACTS_ERROR
} from "es6!src/ducks/contact_list/types";

export const fetch = () => {
  return {
    type: FETCH_CONTACTS
  };
};

export const fetchFulfilled = response => {
  return { type: FETCH_CONTACTS_FULFILLED, payload: response.data };
};

export const fetchError = error => {
  return { type: FETCH_CONTACTS_ERROR, payload: error };
};
