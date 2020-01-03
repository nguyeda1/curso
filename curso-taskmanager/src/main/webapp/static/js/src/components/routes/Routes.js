import React from "react";
import { Switch, Route } from "react-router-dom";
import PrivateRoute from "es6!src/components/routes/PrivateRoute";
import CaseRoutes from "es6!src/components/routes/CaseRoutes";
import TaskRoutes from "es6!src/components/routes/TaskRoutes";
import WorklogRoutes from "es6!src/components/routes/WorklogRoutes";
import ActivityRoutes from "es6!src/components/routes/ActivityRoutes";
import MoreContainer from "es6!src/containers/more/MoreContainer";
import LoginContainer from "es6!src/containers/login/LoginContainer";
import { withStyles } from "material-ui";
import PropTypes from "prop-types";

const Routes = props => {
  const { classes } = props;
  return (
    <Switch>
      <PrivateRoute path="/case" component={CaseRoutes} />
      <PrivateRoute path="/task" component={TaskRoutes} />
      <PrivateRoute path="/activity" component={ActivityRoutes} />
      <PrivateRoute path="/more" component={MoreContainer} />
      <Route path="/login" component={LoginContainer} />
      <Route path="/" component={LoginContainer} />
    </Switch>
  );
};

const styles = {};

Routes.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(Routes);
