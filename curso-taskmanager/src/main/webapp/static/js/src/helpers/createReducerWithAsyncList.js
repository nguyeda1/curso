import {
  pushUniqueElements,
  insertUniqueElements
} from "es6!src/helpers/addUniqueElementToArray";
import updateArray from "es6!src/helpers/updateArray";

const initialState = {
  data: [],
  total: 0,
  page: 0,
  fetched: false,
  fetching: false,
  moreToLoad: true,
  error: undefined
};

const createReducerWithAsyncList = (reducerName = "") => {
  return (state = initialState, action) => {
    const { type, payload } = action;
    switch (type) {
      case `FETCH_${reducerName}`: {
        return {
          ...state,
          fetched: false,
          moreToLoad: true,
          page: 0,
          data: []
        };
      }
      case `FETCH_${reducerName}_ERROR`: {
        return {
          ...state,
          error: action.payload
        };
      }

      case `FETCH_${reducerName}_FULFILLED`: {
        const { data, total } = action.payload;

        const moreToLoad = data.length + state.data.length < total;
        return {
          ...state,
          data,
          total,
          moreToLoad,
          page: state.page + 1,
          fetched: true,
          fetching: false
        };
      }

      case `FETCH_NEXT_${reducerName}_FULFILLED`: {
        const { data, total } = action.payload;
        const moreToLoad = data.length + state.data.length < total;
        return {
          ...state,
          data: pushUniqueElements(state.data, data),
          total,
          moreToLoad,
          page: state.page + 1,
          fetched: true,
          fetching: false
        };
      }
      case `REFRESH_${reducerName}_FULFILLED`: {
        const { data, total } = action.payload;
        const moreToLoad = data.length + state.data.length < total;
        return {
          ...state,
          data: insertUniqueElements(state.data, data),
          total,
          moreToLoad
        };
      }
      case `SEARCH_BY_ID_${reducerName}`: {
        return {
          ...state,
          data: [],
          moreToLoad: true
        };
      }
      case `SEARCH_BY_ID_${reducerName}_FULFILLED`: {
        const data =
          Object.keys(action.payload).length === 0 &&
          action.payload.constructor === Object
            ? []
            : [action.payload];
        return {
          ...state,
          data,
          moreToLoad: false,
          total: data.length
        };
      }
      case `SEARCH_${reducerName}`: {
        return {
          ...state,
          data: [],
          moreToLoad: true
        };
      }
      case `SEARCH_${reducerName}_FULFILLED`: {
        return {
          ...state,
          data: action.payload,
          moreToLoad: false,
          total: action.payload.length
        };
      }
      case `ADD_ITEM_${reducerName}_FULFILLED`: {
        return {
          ...state,
          data: [action.payload, ...state.data]
        };
      }

      case `UPDATE_${reducerName}_FULFILLED`: {
        const item = action.payload;
        const data = updateArray(state.data, item);
        return Object.assign({}, state, { data });
      }
    }
    return state;
  };
};

export default createReducerWithAsyncList;
