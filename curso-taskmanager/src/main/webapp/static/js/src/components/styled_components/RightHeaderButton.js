import React from "react";
import { withStyles, IconButton, Button } from "material-ui";
import PropTypes from "prop-types";
import LinkButton from "es6!src/components/styled_components/LinkButton";

const RightHeaderButton = props => {
  const { classes, children, label } = props;
  const button = label ? (
    <LinkButton {...props}>{children}</LinkButton>
  ) : (
    <IconButton {...props} className={classes.button}>
      {children}
    </IconButton>
  );
  return button;
};

const styles = {
  button: { width: "inherit" }
};

RightHeaderButton.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(RightHeaderButton);
