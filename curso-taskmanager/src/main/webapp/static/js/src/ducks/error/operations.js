import * as actions from "es6!src/ducks/error/actions";

export const close = () => {
  return dispatch => {
    dispatch(actions.close());
  };
};
