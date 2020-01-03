import React from "react";
import { withStyles, CircularProgress } from "material-ui";
import PropTypes from "prop-types";

const Loader = props => {
  const { classes } = props;
  return <CircularProgress {...props} className={classes.middle} />;
};

const styles = {
  middle: {
    display: "flex",
    flexDirection: "column",
    marginLeft: "auto",
    marginRight: "auto"
  }
};

Loader.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(Loader);
