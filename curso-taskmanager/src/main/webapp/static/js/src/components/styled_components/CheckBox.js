import React from "react";
import { withStyles, Checkbox, Icon } from "material-ui";
import PropTypes from "prop-types";

const CheckBox = props => {
  const { classes } = props;
  return (
    <Checkbox
      className={classes.size}
      classes={{
        root: classes.root,
        checked: classes.checked
      }}
      icon={<Icon className={classes.sizeIcon}>panorama_fish_eye</Icon>}
      checkedIcon={<Icon className={classes.sizeIcon}>check_circle</Icon>}
      {...props}
    />
  );
};

const styles = {
  root: {
    "&$checked": {
      color: "#28e07b"
    }
  },
  item: {
    height: 50
  },
  checked: {},
  size: {
    width: 30,
    height: 30
  },
  sizeIcon: {
    fontSize: 30
  }
};

CheckBox.propTypes = {
  data: PropTypes.object,
  checked: PropTypes.bool,
  classes: PropTypes.object
};

export default withStyles(styles)(CheckBox);
