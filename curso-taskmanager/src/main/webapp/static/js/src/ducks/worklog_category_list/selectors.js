import { createSelector } from "reselect";

export const worklogCategoryListSelector = state =>
  state.worklogCategoryList.categories;
export const fetchedSelector = state => state.worklogCategoryList.fetched;
// export const selectedSelector = createSelector(
//   caseDetailGeneralSelectors.caseTagsSelector,
//   caseTags => caseTags
// );
