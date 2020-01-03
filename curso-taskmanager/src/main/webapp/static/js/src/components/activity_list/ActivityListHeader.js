import React from "react";
import { Toolbar, withStyles } from "material-ui";
import PropTypes from "prop-types";
import HeaderTitle from "es6!src/components/styled_components/HeaderTitle";

const ActivityListHeader = props => {
  const { classes } = props;
  return (
    <Toolbar>
      <HeaderTitle>Activities</HeaderTitle>
    </Toolbar>
  );
};

const styles = {};

ActivityListHeader.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(ActivityListHeader);
