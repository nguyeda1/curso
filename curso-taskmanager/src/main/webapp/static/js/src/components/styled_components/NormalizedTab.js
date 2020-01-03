import React from "react";
import { withStyles, Tab } from "material-ui";
import PropTypes from "prop-types";

const NormalizedTab = props => {
  const { classes } = props;
  return <Tab {...props} classes={{ root: classes.tabRoot }} />;
};

const styles = {
  tabRoot: {
    textTransform: "initial"
  }
};

NormalizedTab.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(NormalizedTab);
