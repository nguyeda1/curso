import { POST_PHOTO, GET, POST, DELETE, API_URL } from "es6!src/helpers/ajax";

import * as actions from "es6!src/ducks/case_detail/general/actions";

export const fetchById = id => {
  return dispatch => {
    dispatch(actions.fetch());

    GET(API_URL + "/case/" + id)
      .then(response => {
        dispatch(actions.fetchFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchError(error));
      });
  };
};

export const followCase = id => {
  return dispatch => {
    dispatch(actions.follow());
    POST(API_URL + "/case/" + id + "/follow")
      .then(response => {
        dispatch(actions.followFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.followError(error));
      });
  };
};

export const uploadPhoto = (id, photo) => {
  return dispatch => {
    const caseIdQuery = "?caseId=" + id;
    const meta = { id, photo };
    dispatch(actions.upload(meta));
    POST_PHOTO(API_URL + "/case/upload" + caseIdQuery, photo)
      .then(response => {
        dispatch(actions.uploadFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.uploadError(error));
      });
  };
};

export const deletePhoto = photo => {
  return dispatch => {
    dispatch(actions.removePhoto(photo));
    const pathQuery = "?path=" + photo.path;
    DELETE(API_URL + "/case/remove-photo" + pathQuery)
      .then(response => {
        dispatch(actions.removePhotoFulfilled(photo));
      })
      .catch(error => {
        dispatch(actions.removePhotoError(error));
      });
  };
};

export const fetchPhotos = id => {
  return dispatch => {
    const caseIdQuery = "?caseId=" + id;
    const meta = { id };
    dispatch(actions.fetchPhotos(meta));
    GET(API_URL + "/case/photo-list" + caseIdQuery)
      .then(response => {
        dispatch(actions.fetchPhotosFulfilled(response));
      })
      .catch(error => {
        dispatch(actions.fetchPhotosError(error));
      });
  };
};
