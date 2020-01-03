import React from "react";
import { withStyles, Badge } from "material-ui";
import PropTypes from "prop-types";

const RightBadge = props => {
  const { classes, children } = props;
  return (
    <Badge className={classes.padding} color="secondary" {...props}>
      {children}
    </Badge>
  );
};

const styles = theme => ({
  padding: {
    padding: `0 ${theme.spacing.unit * 2}px`
  }
});

RightBadge.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(RightBadge);
