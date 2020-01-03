import { taskDetailTypes } from "es6!src/ducks/task_detail/index";
import { GET, API_URL } from "es6!src/helpers/ajax";
import {
  UPLOAD_TASK_PHOTO_FULFILLED,
  UPLOAD_TASK_PHOTO_ERROR,
  UPLOAD_TASK_PHOTO,
  REMOVE_TASK_PHOTO,
  REMOVE_TASK_PHOTO_FULFILLED,
  REMOVE_TASK_PHOTO_ERROR,
  FETCH_TASK_PHOTOS_ERROR,
  FETCH_TASK_PHOTOS,
  FETCH_TASK_PHOTOS_FULFILLED
} from "es6!src/ducks/task_detail/general/types";

export const fetch = () => {
  return {
    type: taskDetailTypes.FETCH_TASK_DETAIL
  };
};

export const fetchFulfilled = response => {
  return {
    type: taskDetailTypes.FETCH_TASK_DETAIL_FULFILLED,
    payload: response.data
  };
};

export const fetchError = error => {
  return { type: taskDetailTypes.FETCH_TASK_DETAIL_ERROR, payload: error };
};

export const upload = meta => {
  return {
    type: UPLOAD_TASK_PHOTO,
    meta
  };
};

export const uploadFulfilled = response => {
  return { type: UPLOAD_TASK_PHOTO_FULFILLED, payload: response.data };
};

export const uploadError = error => {
  return actionError(UPLOAD_TASK_PHOTO_ERROR, error);
};

export const fetchPhotos = meta => {
  return {
    type: FETCH_TASK_PHOTOS,
    meta
  };
};

export const fetchPhotosFulfilled = response => {
  return { type: FETCH_TASK_PHOTOS_FULFILLED, payload: response.data };
};
export const fetchPhotosError = error => {
  return { type: FETCH_TASK_PHOTOS_ERROR, payload: error };
};

export const removePhoto = photo => {
  return { type: REMOVE_TASK_PHOTO, payload: photo };
};

export const removePhotoFulfilled = response => {
  return { type: REMOVE_TASK_PHOTO_FULFILLED, payload: response };
};

export const removePhotoError = error => {
  return { type: REMOVE_TASK_PHOTO_ERROR, payload: error };
};
