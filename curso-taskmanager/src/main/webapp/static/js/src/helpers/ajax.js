import axios from "axios";
import { unsetCurrentUser } from "es6!src/helpers/localStorage";

axios.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    if (401 === error.response.status) {
      window.location.hash = "#/login";
    }
    return Promise.reject(error);
  }
);

//For mobile device
//export const API_URL = "http://192.168.10.106:8080/tasks/curso-dev/api";
export const API_URL = typeof base !== "undefined" ? base + "api" : "api";

export const whoami = () => {
  return axios({
    url: API_URL + "/user/whoami",
    timeout: 40000,
    method: "get",
    responseType: "json"
  });
};

export const GET = url => {
  return axios({
    url,
    timeout: 40000,
    method: "get",
    responseType: "json"
  });
};

export const POST = (url, data) => {
  return axios({
    url,
    timeout: 40000,
    method: "post",
    responseType: "json",
    data
  });
};

export const DELETE = url => {
  return axios({
    url,
    timeout: 20000,
    method: "delete"
  });
};

export const POST_PHOTO = (url, photos) => {
  var data = new FormData();
  photos.map(photo => {
    data.append("image[]", photo, photo.name);
  });

  return axios({
    url,
    method: "post",
    responseType: "json",
    data,
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
};

export const actionFulfilled = (type, response) => {
  return { type, payload: response.data };
};

export const actionError = (type, error) => {
  return { type, payload: error };
};
