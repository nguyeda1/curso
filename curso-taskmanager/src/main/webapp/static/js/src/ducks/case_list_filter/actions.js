import { FILTER_CASES } from "es6!src/ducks/case_list_filter/types";

export const filter = state => {
  return { type: FILTER_CASES, payload: state };
};
