import React from "react";
import { withStyles, Chip, Toolbar } from "material-ui";
import PropTypes from "prop-types";

const SelectChipPanel = props => {
  const { classes } = props;
  return <Toolbar {...props} className={classes.chipSelect} />;
};

const styles = {
  chipSelect: {
    display: "flex",
    justifyContent: "space-evenly",
    paddingLeft: 0,
    paddingRight: 0,
    paddingTop: 5
  }
};

SelectChipPanel.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(SelectChipPanel);
