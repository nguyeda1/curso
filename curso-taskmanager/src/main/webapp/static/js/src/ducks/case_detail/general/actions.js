import {
  FETCH_CASE_DETAIL_ERROR,
  FETCH_CASE_DETAIL,
  UPLOAD_CASE_PHOTO_FULFILLED,
  UPLOAD_CASE_PHOTO_ERROR,
  UPLOAD_CASE_PHOTO,
  FETCH_PHOTOS_FULFILLED,
  REMOVE_CASE_PHOTO,
  REMOVE_CASE_PHOTO_FULFILLED,
  REMOVE_CASE_PHOTO_ERROR,
  FETCH_CASE_PHOTOS_ERROR,
  FETCH_CASE_PHOTOS,
  FETCH_CASE_PHOTOS_FULFILLED,
  FOLLOW_CASE,
  FOLLOW_CASE_ERROR,
  FOLLOW_CASE_FULFILLED
} from "es6!src/ducks/case_detail/general/types";
import { caseDetailTypes } from "es6!src/ducks/case_detail/index";
import {
  actionFulfilled,
  actionError,
  GET,
  API_URL
} from "es6!src/helpers/ajax";

export const fetch = () => {
  return {
    type: FETCH_CASE_DETAIL
  };
};

export const fetchFulfilled = response => {
  return actionFulfilled(caseDetailTypes.FETCH_CASE_DETAIL_FULFILLED, response);
};

export const fetchError = error => {
  return actionError(FETCH_CASE_DETAIL_ERROR, error);
};

export const upload = meta => {
  return {
    type: UPLOAD_CASE_PHOTO,
    meta
  };
};

export const uploadFulfilled = response => {
  return { type: UPLOAD_CASE_PHOTO_FULFILLED, payload: response.data };
};

export const uploadError = error => {
  return actionError(UPLOAD_CASE_PHOTO_ERROR, error);
};

export const fetchPhotos = meta => {
  return {
    type: FETCH_CASE_PHOTOS,
    meta
  };
};

export const fetchPhotosFulfilled = response => {
  return { type: FETCH_CASE_PHOTOS_FULFILLED, payload: response.data };
};
export const fetchPhotosError = error => {
  return { type: FETCH_CASE_PHOTOS_ERROR, payload: error };
};

export const removePhoto = photo => {
  return { type: REMOVE_CASE_PHOTO, payload: photo };
};

export const removePhotoFulfilled = response => {
  return { type: REMOVE_CASE_PHOTO_FULFILLED, payload: response };
};

export const removePhotoError = error => {
  return { type: REMOVE_CASE_PHOTO_ERROR, payload: error };
};

export const follow = id => {
  return { type: FOLLOW_CASE, payload: id };
};

export const followFulfilled = response => {
  return { type: FOLLOW_CASE_FULFILLED, payload: response.data };
};

export const followError = error => {
  return { type: FOLLOW_CASE_ERROR, payload: error };
};
