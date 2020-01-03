import { createSelector } from "reselect";
import groupByProperty from "es6!src/helpers/groupByProperty";
import { getCurrentUser } from "es6!src/helpers/localStorage";

const myCasesSelector = state => state.caseList.MY_CASES;
const allSelector = state => state.caseList.ALL;
const newSelector = state => state.caseList.NEW;
const inProgressSelector = state => state.caseList.IN_PROGRESS;
const reviewSelector = state => state.caseList.REVIEW;
const doneSelector = state => state.caseList.DONE;

export const filterValueSelector = (state, props) => {
  return props.filter;
};

const makeCaseTypeSelector = () =>
  createSelector(
    filterValueSelector,
    myCasesSelector,
    allSelector,
    newSelector,
    inProgressSelector,
    reviewSelector,
    doneSelector,
    (
      filter,
      myData,
      allData,
      newData,
      inProgressData,
      reviewData,
      doneData
    ) => {
      switch (filter) {
        case "MY_CASES": {
          return myData;
        }
        case "ALL": {
          return allData;
        }
        case "NEW": {
          return newData;
        }
        case "IN_PROGRESS": {
          return inProgressData;
        }
        case "REVIEW": {
          return reviewData;
        }
        case "DONE": {
          return doneData;
        }
      }
    }
  );

const caseTypeSelector = makeCaseTypeSelector();

export const dataSelector = createSelector(caseTypeSelector, type => {
  return type.data;
});

export const totalSelector = createSelector(
  caseTypeSelector,
  type => type.total
);

export const pageSelector = createSelector(caseTypeSelector, type => type.page);

export const moreToLoadSelector = createSelector(
  caseTypeSelector,
  type => type.moreToLoad
);

export const fetchedSelector = createSelector(
  caseTypeSelector,
  type => type.fetched
);
