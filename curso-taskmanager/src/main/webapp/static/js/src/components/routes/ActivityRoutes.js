import React from "react";
import { Switch, Route } from "react-router-dom";
import ActivityListContainer from "es6!src/containers/activity_list/ActivityListContainer";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import PrivateRoute from "es6!src/components/routes/PrivateRoute";

const ActivityRoutes = props => {
  const { match } = props;
  return (
    <Switch>
      <PrivateRoute
        exact
        path={`${match.url}/list`}
        render={p => (
          <ActivityListContainer
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
        exact
        path={`${match.url}`}
        render={p => (
          <ActivityListContainer
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
        component={ActivityListContainer}
      />
    </Switch>
  );
};

export default ActivityRoutes;
