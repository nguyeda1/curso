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

const CaseHistoryItem = props => {
  const { classes, data, redirect } = props;

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

  return (
    <div onClick={() => redirect(data)}>
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

CaseHistoryItem.propTypes = {
  data: PropTypes.object.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(CaseHistoryItem);
