import {
  pushUniqueElements,
  insertUniqueElements
} from "es6!src/helpers/addUniqueElementToArray";
import updateArray from "es6!src/helpers/updateArray";

const initialState = {
  photos: [],
  fetched: false,
  loading: false,
  error: undefined
};

const createReducerWithPhotos = (reducerName = "") => {
  return (state = initialState, action) => {
    const { type, payload } = action;
    switch (type) {
      case `FETCH_${reducerName}_PHOTOS`: {
        return {
          ...state,
          fetched: false,
          loading: true,
          photos: []
        };
      }
      case `FETCH_${reducerName}_PHOTOS_ERROR`: {
        return {
          ...state,
          error: action.payload
        };
      }

      case `FETCH_${reducerName}_PHOTOS_FULFILLED`: {
        return {
          ...state,
          photos: action.payload,
          fetched: true,
          loading: false
        };
      }

      case `UPLOAD_${reducerName}_PHOTO`: {
        return { ...state, loading: true };
      }

      case `UPLOAD_${reducerName}_PHOTO_FULFILLED`: {
        var photos = [...state.photos, ...action.payload];
        return { ...state, photos: photos, loading: false };
      }

      case `UPLOAD_${reducerName}_PHOTO_ERROR`: {
        return { ...state, error: action.payload };
      }

      case `REMOVE_${reducerName}_PHOTO_FULFILLED`: {
        var photos = [...state.photos];
        var index = photos.indexOf(action.payload);
        photos.splice(index, 1);
        return { ...state, photos };
      }
    }
    return state;
  };
};

export default createReducerWithPhotos;
