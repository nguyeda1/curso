import React from "react";
import { withStyles, Input, Select, Icon } from "material-ui";
import PropTypes from "prop-types";

const SelectBox = props => {
  const { classes } = props;
  return (
    <Select
      {...props}
      classes={{ root: classes.select }}
      input={<Input readOnly />}
      IconComponent={props => <Icon>keyboard_arrow_right</Icon>}
    />
  );
};

const styles = {
  select: {
    display: "flex"
  }
};

SelectBox.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(SelectBox);
