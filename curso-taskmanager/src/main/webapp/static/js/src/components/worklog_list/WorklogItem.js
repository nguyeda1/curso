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
import moment from "moment";

const WorklogItem = props => {
  const { classes, data, onClick } = props;

  return (
    <ListItem onClick={() => onClick(data.id)}>
      <ListItemText>
        <Typography variant="body1">{`${data.category.name} (${moment(
          data.from
        ).format("HH:mm")} - ${moment(data.to).format("HH:mm")})`}</Typography>
        <Typography variant="caption">
          {`${data.user.username}, ${moment(data.date).format("DD.MM.YYYY")}`}
        </Typography>
      </ListItemText>
    </ListItem>
  );
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

WorklogItem.propTypes = {
  data: PropTypes.object.isRequired,
  mini: PropTypes.bool,
  classes: PropTypes.object
};

export default withStyles(styles)(WorklogItem);
