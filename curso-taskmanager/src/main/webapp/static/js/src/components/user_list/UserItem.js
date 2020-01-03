import React from "react";
import {
  Avatar,
  ListItemText,
  Typography,
  withStyles,
  ListItem
} from "material-ui";
import BasicItem from "es6!src/components/styled_components/BasicItem";
import PropTypes from "prop-types";
import SquareAvatar from "es6!src/components/styled_components/SquareAvatar";

const UserItem = props => {
  const { classes, data, onClick } = props;
  const owner = `${data.fullname}`;
  const user = data ? (
    <ListItem onClick={() => onClick(data)}>
      <SquareAvatar>U</SquareAvatar>
      <Typography>{owner}</Typography>
    </ListItem>
  ) : null;
  return user;
};

const styles = {};

UserItem.propTypes = {
  data: PropTypes.object.isRequired,
  mini: PropTypes.bool,
  classes: PropTypes.object
};

export default withStyles(styles)(UserItem);
