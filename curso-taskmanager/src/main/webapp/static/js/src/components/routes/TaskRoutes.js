import React from "react";
import { Switch, Route } from "react-router-dom";
import TaskListContainer from "es6!src/containers/task_list/TaskListContainer";
import TaskDetailContainer from "es6!src/containers/task_detail/TaskDetailContainer";
import PrivateRoute from "es6!src/components/routes/PrivateRoute";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import WorklogEditContainer from "es6!src/containers/worklog_edit/WorklogEditContainer";
import WorklogDetailContainer from "es6!src/containers/worklog_detail/WorklogDetailContainer";

const TaskRoutes = props => {
  const { match } = props;
  return (
    <Switch>
      <PrivateRoute
        path={`${match.url}/list`}
        render={p => (
          <TaskListContainer
            renderAsyncScroll={props => (
              <InfiniteScroller
                loadMore={props.fetch}
                hasMore={props.hasMore}
                isReverse={props.reverse}
              >
                {props.data.map(c => (
                  <props.renderComponent
                    {...props.componentProps}
                    key={c.id}
                    data={c}
                  />
                ))}
              </InfiniteScroller>
            )}
            {...p}
          />
        )}
      />
      <PrivateRoute
        path={`${match.url}/view/:id`}
        component={TaskDetailContainer}
      />

      <PrivateRoute
        exact
        path={`${match.url}`}
        render={p => (
          <TaskListContainer
            renderAsyncScroll={props => (
              <InfiniteScroller
                loadMore={props.fetch}
                hasMore={props.hasMore}
                isReverse={props.reverse}
              >
                {props.data.map(c => (
                  <props.renderComponent
                    {...props.componentProps}
                    key={c.id}
                    data={c}
                  />
                ))}
              </InfiniteScroller>
            )}
            {...p}
          />
        )}
      />
    </Switch>
  );
};

export default TaskRoutes;