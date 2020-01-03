import React from "react";
import { Switch, Route } from "react-router-dom";
import TaskListContainer from "es6!src/containers/task_list/TaskListContainer";
import TaskDetailContainer from "es6!src/containers/task_detail/TaskDetailContainer";
import PrivateRoute from "es6!src/components/routes/PrivateRoute";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import WorklogEditContainer from "es6!src/containers/worklog_edit/WorklogEditContainer";
import WorklogDetailContainer from "es6!src/containers/worklog_detail/WorklogDetailContainer";

const WorklogRoutes = props => {
  const { match } = props;
  return (
    <Switch>
      <PrivateRoute
        path={`${match.url}/edit/:fetchId`}
        render={p => <WorklogEditContainer {...p} />}
      />
      <PrivateRoute
        path={`${match.url}/view/:id`}
        render={p => <WorklogDetailContainer {...p} />}
      />
    </Switch>
  );
};

export default WorklogRoutes;
