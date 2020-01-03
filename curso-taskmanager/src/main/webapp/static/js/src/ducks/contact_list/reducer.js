import {
  FETCH_CONTACTS,
  FETCH_CONTACTS_FULFILLED,
  FETCH_CONTACTS_ERROR
} from "es6!src/ducks/contact_list/types";

const initialState = {
  contacts: [],
  fetching: false,
  fetched: false,
  error: null
};
const reducer = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_CONTACTS: {
      return Object.assign({}, state, {
        fetching: true
      });
    }
    case FETCH_CONTACTS_ERROR: {
      return Object.assign({}, state, {
        error: action.payload
      });
    }
    case FETCH_CONTACTS_FULFILLED: {
      return Object.assign({}, state, {
        contacts: action.payload,
        fetched: true,
        fetching: false
      });
    }
  }

  return state;
};

export default reducer;
