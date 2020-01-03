import React from "react";
import { withStyles, Typography, Icon } from "material-ui";
import PropTypes from "prop-types";
import normalizeText from "es6!src/helpers/normalizeText";

const Priority = props => {
  const { classes, children, showText } = props;
  const arrow = children === "LOW" ? "arrow_downward" : "arrow_upward";
  const priority = showText ? (
    <Typography className={classes.priority}>
      <Icon className={`${classes[children]}`}>{arrow}</Icon>
      {normalizeText(children)}
    </Typography>
  ) : (
    <Icon className={`${classes[children]}`}>{arrow}</Icon>
  );
  return priority;
};

const styles = {
  priority: {
    display: "inline-flex",
    alignItems: "flex-end"
  },

  HIGH: {
    color: "red"
  },
  MEDIUM: {
    color: "#ffc107"
  },
  LOW: {
    color: "green"
  }
};

Priority.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(Priority);
