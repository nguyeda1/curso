import React from "react";
import { Switch, Route } from "react-router-dom";
import CaseListContainer from "es6!src/containers/case_list/CaseListContainer";
import CaseDetailContainer from "es6!src/containers/case_detail/CaseDetailContainer";
import FeedbackEditContainer from "es6!src/containers/feedback_edit/FeedbackEditContainer";
import CaseEditContainer from "es6!src/containers/case_edit/CaseEditContainer";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import PrivateRoute from "es6!src/components/routes/PrivateRoute";

const CaseRoutes = props => {
  const { match } = props;
  return (
    <Switch>
      <PrivateRoute path={`${match.url}/list`} component={CaseListContainer} />
      <PrivateRoute
        path={`${match.url}/view/:id`}
        component={CaseDetailContainer}
      />
      <PrivateRoute
        path={`${match.url}/edit/:id`}
        render={props => <CaseEditContainer {...props} existing />}
      />
      <PrivateRoute path={`${match.url}/new`} component={CaseEditContainer} />
      <PrivateRoute exact path={`${match.url}`} component={CaseListContainer} />
    </Switch>
  );
};

export default CaseRoutes;
