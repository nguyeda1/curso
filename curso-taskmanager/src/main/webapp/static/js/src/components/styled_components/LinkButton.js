import React from "react";
import { withStyles, Button } from "material-ui";
import PropTypes from "prop-types";

const LinkButton = props => {
  const { classes, children, onClick, ...other } = props;
  return (
    <Button color="primary" onClick={onClick} className={classes.link}>
      {children}
    </Button>
  );
};

const styles = {
  link: {
    textDecoration: "underline",
    textTransform: "initial",
    padding: 0
  }
};

LinkButton.propTypes = {
  onClick: PropTypes.func,
  classes: PropTypes.object
};

export default withStyles(styles)(LinkButton);
