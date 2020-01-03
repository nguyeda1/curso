import React from "react";
import { withStyles, Typography } from "material-ui";
import PropTypes from "prop-types";

const HeaderTitle = props => {
  const { classes, children } = props;
  return (
    <Typography variant="title" {...props} className={classes.header}>
      {children}
    </Typography>
  );
};

const styles = {
  header: { flex: 1 }
};

HeaderTitle.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(HeaderTitle);
