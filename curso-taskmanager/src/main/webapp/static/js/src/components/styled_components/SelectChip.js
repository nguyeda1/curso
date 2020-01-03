import React from "react";
import { withStyles, Button, Toolbar } from "material-ui";
import PropTypes from "prop-types";

const SelectChip = props => {
  const { classes, clicked, label, onClick, value } = props;
  return (
    <Button
      onClick={onClick}
      value={value}
      variant="contained"
      color={clicked ? "primary" : "default"}
      className={classes.grow}
    >
      {label}
    </Button>
  );
};

const styles = {
  grow: {
    flexGrow: 1
  }
};

SelectChip.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(SelectChip);
