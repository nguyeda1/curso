import React from "react";
import { Avatar, ListItemText, Typography, withStyles } from "material-ui";
import Item from "es6!src/components/styled_components/Item";
import PropTypes from "prop-types";

const FixAboveNav = props => {
  const { classes, children } = props;

  return <div className={classes.fix}>{children}</div>;
};

const styles = {
  fix: {
    position: "absolute",
    bottom: "56px",
    width: "100%"
  }
};

FixAboveNav.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(FixAboveNav);
