import * as types from "es6!src/ducks/error/types";

export const close = () => {
  return {
    type: types.ERROR_DIALOG_CLOSE
  };
};
