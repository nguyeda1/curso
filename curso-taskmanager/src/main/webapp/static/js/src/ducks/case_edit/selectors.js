import { createSelector } from "reselect";

export const caseSelector = state => state.caseEdit.cursoCase;
export const nameSelector = state => {
  const c = state.caseEdit.cursoCase;
  return c.name ? c.name : "";
};
export const descriptionSelector = state => {
  const c = state.caseEdit.cursoCase;
  return c.description ? c.description : "";
};
export const ownerSelector = state => {
  const c = state.caseEdit.cursoCase;
  return c.owner ? c.owner : undefined;
};
export const bookingSelector = state => {
  const c = state.caseEdit.cursoCase;
  return c.booking ? c.booking : undefined;
};
export const listingSelector = state => {
  const c = state.caseEdit.cursoCase;
  return c.listing ? c.listing : undefined;
};
export const fetchedSelector = state => state.caseEdit.fetched;
