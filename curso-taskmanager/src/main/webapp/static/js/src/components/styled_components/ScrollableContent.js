import React from "react";
import { List, withStyles } from "material-ui";
import PropTypes from "prop-types";

const ScrollableContent = props => {
  const { classes, children } = props;
  return (
    <List {...props} className={classes.scrollable}>
      {children}
    </List>
  );
};

const styles = {
  scrollable: {
    overflow: "auto",
    height: "100%"
  }
};

ScrollableContent.propTypes = {
  children: PropTypes.node,
  classes: PropTypes.object
};

export default withStyles(styles)(ScrollableContent);
