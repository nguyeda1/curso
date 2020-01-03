import React from "react";
import {
  Avatar,
  ListItemText,
  ListItem,
  Typography,
  Divider,
  withStyles
} from "material-ui";
import BasicItem from "es6!src/components/styled_components/BasicItem";
import PropTypes from "prop-types";
import SquareAvatar from "es6!src/components/styled_components/SquareAvatar";

const ListingItem = props => {
  const { classes, data, mini, onClick } = props;
  const listingItem = mini ? (
    <div className={classes.mini}>
      <Avatar className={classes.minAvatar}>L</Avatar>
      <Typography variant="caption">{data.name}</Typography>
    </div>
  ) : (
    <ListItem onClick={() => (onClick ? onClick(data) : null)}>
      <SquareAvatar>L</SquareAvatar>
      <ListItemText>
        <Typography variant="body1">{data.name}</Typography>
        <Typography variant="caption">{data.locality}</Typography>
      </ListItemText>
    </ListItem>
  );

  return listingItem;
};

const styles = {
  mini: {
    display: "flex",
    alignItems: "center"
  },

  minAvatar: {
    borderRadius: 2,
    width: 10,
    height: 10,
    marginRight: 5
  }
};

ListingItem.propTypes = {
  data: PropTypes.object.isRequired,
  mini: PropTypes.bool,
  classes: PropTypes.object
};

export default withStyles(styles)(ListingItem);
