import React from "react";
import { withStyles, ListItem } from "material-ui";
import PropTypes from "prop-types";

const Item = props => {
  const { classes, children } = props;
  return (
    <ListItem className={classes.noPadding} {...props}>
      {children}
    </ListItem>
  );
};

const styles = {
  noPadding: { marginTop: "5px", paddingLeft: 0, paddingRight: 0 }
};

Item.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(Item);
