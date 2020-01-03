import getAsyncListTypes from "es6!src/helpers/getAsyncListTypes";

const MY_CASES_NAME = "CASE_FILTER_MY_CASES";
const MY_CASES = {
  ...getAsyncListTypes(MY_CASES_NAME)
};

const ALL_NAME = "CASE_FILTER_ALL";
const ALL = {
  ...getAsyncListTypes(ALL_NAME)
};

const NEW_NAME = "CASE_FILTER_NEW";
const NEW = {
  ...getAsyncListTypes(NEW_NAME)
};

const IN_PROGRESS_NAME = "CASE_FILTER_IN_PROGRESS";
const IN_PROGRESS = {
  ...getAsyncListTypes(IN_PROGRESS_NAME)
};

const REVIEW_NAME = "CASE_FILTER_REVIEW";
const REVIEW = {
  ...getAsyncListTypes(REVIEW_NAME)
};

const DONE_NAME = "CASE_FILTER_DONE";
const DONE = {
  ...getAsyncListTypes(REVIEW_NAME)
};

export default { MY_CASES, ALL, NEW, IN_PROGRESS, REVIEW, DONE };
