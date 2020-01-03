import React from "react";
import { withStyles } from "material-ui";
import PropTypes from "prop-types";

const Page = props => {
  const { children, classes } = props;
  return <div className={classes.content}>{children}</div>;
};

const styles = {
  content: {
    display: "flex",
    flexDirection: "column",
    height: "inherit"
  }
};

Page.propTypes = {
  classes: PropTypes.object,
  children: PropTypes.node
};

export default withStyles(styles)(Page);
