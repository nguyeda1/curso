import React from "react";
import {
  Avatar,
  ListItemText,
  Typography,
  withStyles,
  ListItem
} from "material-ui";
import BasicItem from "es6!src/components/styled_components/BasicItem";
import moment from "moment";
import PropTypes from "prop-types";
import SquareAvatar from "es6!src/components/styled_components/SquareAvatar";

const BookingItem = props => {
  const { classes, data, onClick } = props;
  const start = data.startDate
    ? moment(data.startDate).format("DD.MM.YYYY")
    : null;
  const end = data.endDate ? moment(data.endDate).format("DD.MM.YYYY") : null;
  const stayInterval = start && end ? start + " - " + end : null;
  const canceled = data.canceled ? "CANCELED" : "";
  const booking = (
    <ListItem onClick={() => onClick(data)}>
      <SquareAvatar>B</SquareAvatar>
      <ListItemText>
        <Typography variant="body1">{stayInterval}</Typography>
        <Typography variant="caption">{data.guestName}</Typography>
        <Typography className={classes.canceled} variant="caption">
          {canceled}
        </Typography>
      </ListItemText>
    </ListItem>
  );

  return booking;
};

const styles = { canceled: { color: "red" } };

BookingItem.propTypes = {
  data: PropTypes.object.isRequired,
  mini: PropTypes.bool,
  classes: PropTypes.object
};

export default withStyles(styles)(BookingItem);
