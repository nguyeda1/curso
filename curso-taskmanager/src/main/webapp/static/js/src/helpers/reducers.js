import { combineReducers } from "redux";

import caseListFilter from "es6!src/ducks/case_list_filter/index";
import caseList from "es6!src/ducks/case_list/index";
import caseDetailTabPanel from "es6!src/ducks/case_detail/tab_panel/index";
import caseDetailGeneral from "es6!src/ducks/case_detail/general/index";
import caseDetailTaskList from "es6!src/ducks/case_detail/tasks/index";
import caseDetailHistory from "es6!src/ducks/case_detail/history/index";
import caseDetailFeedback from "es6!src/ducks/case_detail/feedback/index";
import caseDetailComments from "es6!src/ducks/case_detail/comments/index";
import error from "es6!src/ducks/error/index";
import feedbackEdit from "es6!src/ducks/feedback_edit/index";
import taskList from "es6!src/ducks/task_list/index";
import navigation from "es6!src/ducks/navigation/index";
import session from "es6!src/ducks/session/index";
import activityList from "es6!src/ducks/activity_list/index";
import contactList from "es6!src/ducks/contact_list/index";
import caseEdit from "es6!src/ducks/case_edit/index";
import listingList from "es6!src/ducks/listing_list/index";
import bookingList from "es6!src/ducks/booking_list/index";
import userList from "es6!src/ducks/user_list/index";
import tagList from "es6!src/ducks/tag_list/index";
import taskDetailGeneral from "es6!src/ducks/task_detail/general/index";
import taskDetailTabPanel from "es6!src/ducks/task_detail/tab_panel/index";
import taskDetailChecklist from "es6!src/ducks/task_detail/checklist/index";
import taskDetailComments from "es6!src/ducks/task_detail/comments/index";
import taskDetailHeader from "es6!src/ducks/task_detail/header/index";
import worklogList from "es6!src/ducks/worklog_list/index";
import worklogEdit from "es6!src/ducks/worklog_edit/index";
import worklogCategoryList from "es6!src/ducks/worklog_category_list/index";
import worklogDetail from "es6!src/ducks/worklog_detail/index";

const combinedReducers = combineReducers({
  activityList,
  bookingList,
  caseEdit,
  caseDetailTabPanel,
  caseDetailGeneral,
  caseDetailFeedback,
  caseDetailHistory,
  caseDetailTaskList,
  caseDetailComments,
  error,
  feedbackEdit,
  caseListFilter,
  caseList,
  contactList,
  listingList,
  navigation,
  session,
  taskList,
  tagList,
  taskDetailGeneral,
  taskDetailTabPanel,
  taskDetailChecklist,
  taskDetailComments,
  taskDetailHeader,
  userList,
  worklogCategoryList,
  worklogDetail,
  worklogEdit,
  worklogList
});

export default combinedReducers;
