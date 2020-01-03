import React from "react";
import { withStyles, Typography, Toolbar } from "material-ui";
import PropTypes from "prop-types";
import normalizeText from "es6!src/helpers/normalizeText";

const StateTag = props => {
  const { classes, children } = props;

  return (
    <Typography
      className={`${classes.tag} ${classes[children]}`}
      variant="caption"
    >
      {normalizeText(children)}
    </Typography>
  );
};

const styles = {
  tag: {
    fontWeight: "bold",
    width: "max-content",
    padding: "3px 15px 3px 15px",
    borderRadius: "5px",
    color: "white",
    whiteSpace: "nowrap"
  },
  NEW: {
    backgroundColor: "rgba(255, 30, 30, 0.5)"
  },
  IN_PROGRESS: {
    backgroundColor: "rgba(0, 44, 206, 0.68)"
  },
  REVIEW: {
    backgroundColor: "rgb(255, 200, 0)"
  },
  DONE: {
    backgroundColor: "rgb(9, 213, 53)"
  },
  NOT_DONE: {
    backgroundColor: "rgba(255, 0, 0, 0.75)"
  },
  PARTIALLY: {
    backgroundColor: "rgb(255, 200, 0)"
  },
  HIGH: {
    backgroundColor: "rgb(9, 213, 53)"
  },
  MEDIUM: {
    backgroundColor: "rgb(9, 213, 53)"
  },
  LOW: {
    backgroundColor: "rgb(9, 213, 53)"
  }
};

StateTag.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(StateTag);
