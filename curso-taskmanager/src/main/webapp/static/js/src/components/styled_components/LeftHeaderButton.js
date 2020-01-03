import React from "react";
import { withStyles, IconButton } from "material-ui";
import PropTypes from "prop-types";

const LeftHeaderButton = props => {
  const { classes, children } = props;
  return (
    <IconButton {...props} className={classes.menuButton}>
      {children}
    </IconButton>
  );
};

const styles = {
  menuButton: {
    marginLeft: -12,
    marginRight: 20
  }
};

LeftHeaderButton.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(LeftHeaderButton);
