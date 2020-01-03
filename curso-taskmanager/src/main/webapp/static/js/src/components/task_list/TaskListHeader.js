import React from "react";
import { Toolbar, withStyles } from "material-ui";
import PropTypes from "prop-types";
import HeaderTitle from "es6!src/components/styled_components/HeaderTitle";

const TaskListHeader = props => {
  const { classes, size } = props;
  return (
    <Toolbar>
      <HeaderTitle>My tasks({size})</HeaderTitle>
    </Toolbar>
  );
};

const styles = {};

TaskListHeader.propTypes = {
  size: PropTypes.number,
  classes: PropTypes.object
};

export default withStyles(styles)(TaskListHeader);
