import { REDIRECT } from "es6!src/ducks/navigation/types";

export const redirect = path => {
  return {
    type: REDIRECT,
    payload: path
  };
};
