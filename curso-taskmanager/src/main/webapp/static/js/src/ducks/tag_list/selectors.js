import { createSelector } from "reselect";

export const tagListSelector = state => state.tagList.tags;
export const fetchedSelector = state => state.tagList.fetched;
// export const selectedSelector = createSelector(
//   caseDetailGeneralSelectors.caseTagsSelector,
//   caseTags => caseTags
// );
