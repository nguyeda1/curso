import React from "react";
import {
  Avatar,
  ListItem,
  ListItemText,
  Typography,
  Divider,
  withStyles
} from "material-ui";
import moment from "moment";
import PropTypes from "prop-types";

const ActivityListItem = props => {
  const { classes, data, isNew, redirect } = props;

  const description = (
    <Typography
      className={classes.lineBreaks}
      variant="body1"
      dangerouslySetInnerHTML={{ __html: data.log }}
    />
  );

  const createdInfo = (
    <Typography variant="caption">
      By <strong>{data.createdBy.username}</strong> on{" "}
      {moment(data.createdOn).format("D. MMMM, HH:mm")}
    </Typography>
  );

  const onClick = () => {
    if (redirect) {
      redirect(data);
    }
  };

  return (
    <div
      onClick={onClick}
      className={isNew(data.createdOn) ? classes.isNew : ""}
    >
      <ListItem>
        <ListItemText>
          {description}
          {createdInfo}
        </ListItemText>
      </ListItem>
      <Divider />
    </div>
  );
};

const styles = {
  isNew: { backgroundColor: "lavender" },
  lineBreaks: { whiteSpace: "pre-line" }
};

ActivityListItem.propTypes = {
  data: PropTypes.object.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(ActivityListItem);
