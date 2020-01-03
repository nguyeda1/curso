import React from "react";
import { Avatar, ListItemText, Typography, withStyles } from "material-ui";
import Item from "es6!src/components/styled_components/Item";
import PropTypes from "prop-types";

const BasicItem = props => {
  const { classes, avatar, children } = props;
  const avatarComponent = avatar ? (
    <Avatar className={classes.avatar}>{avatar}</Avatar>
  ) : null;
  return (
    <Item {...props}>
      {avatarComponent}
      <ListItemText>{children}</ListItemText>
    </Item>
  );
};

const styles = {
  avatar: {
    borderRadius: 10,
    width: 30,
    height: 30
  }
};

BasicItem.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(BasicItem);
