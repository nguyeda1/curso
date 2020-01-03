import React from "react";
import { withStyles, FormControl, Input } from "material-ui";
import PropTypes from "prop-types";

const StyledInput = props => {
  const { classes, children } = props;
  return (
    <Input
      multiline
      rows="1"
      rowsMax="3"
      disableUnderline
      className={classes.container}
      {...props}
      type="text"
    />
  );
};

const styles = {
  container: {
    alignItems: "center",
    width: "inherit",
    borderBottom: "1px solid lightgrey",
    borderTop: "1px solid lightgrey"
  }
};

StyledInput.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(StyledInput);
