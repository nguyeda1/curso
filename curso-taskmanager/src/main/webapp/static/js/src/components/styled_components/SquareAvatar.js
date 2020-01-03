import React from "react";
import { withStyles, Avatar, Toolbar } from "material-ui";
import PropTypes from "prop-types";

const SquareAvatar = props => {
  const { classes, children } = props;
  return (
    <Avatar {...props} className={classes.avatar}>
      {children}
    </Avatar>
  );
};

const styles = {
  avatar: {
    borderRadius: 10,
    width: 30,
    height: 30,
    marginRight: 5
  }
};

SquareAvatar.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(SquareAvatar);
