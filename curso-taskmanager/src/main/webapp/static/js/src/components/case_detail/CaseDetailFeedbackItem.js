import React from "react";
import {
  Icon,
  Avatar,
  ListItem,
  ListItemText,
  Typography,
  Divider,
  withStyles
} from "material-ui";
import moment from "moment";
import PropTypes from "prop-types";

const CaseDetailFeedbackItem = props => {
  const { classes, data } = props;
  const recepient = (
    <strong>
      {data.hostInformed && data.guestInformed
        ? "To host and guest"
        : data.hostInformed
          ? "To host"
          : data.guestInformed ? "To guest" : null}
    </strong>
  );

  const typeIcon = (
    <Icon>
      {data.type === "EMAIL"
        ? "mail_outline"
        : data.type === "PHONE" ? "phone_iphone" : "info"}
    </Icon>
  );

  const contactType =
    data.type === "EMAIL"
      ? " via E-mail"
      : data.type === "PHONE" ? " via Phone" : null;

  const header = (
    <Typography className={classes.header} variant="body1">
      {typeIcon} {recepient} {contactType}
    </Typography>
  );

  const description = (
    <Typography variant="body1">{data.description}</Typography>
  );

  const createdInfo = (
    <Typography variant="caption">
      By <strong>{data.reviewedBy.fullname}</strong> on{" "}
      {moment(data.reviewedOn).format("D. MMMM, HH:mm")}
    </Typography>
  );

  return (
    <div>
      <ListItem>
        <ListItemText>
          {header}
          {description}
          {createdInfo}
        </ListItemText>
      </ListItem>
      <Divider />
    </div>
  );
};

const styles = {
  header: { display: "flex", alignItems: "center", whiteSpace: "pre-wrap" }
};

CaseDetailFeedbackItem.propTypes = {
  data: PropTypes.object.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(CaseDetailFeedbackItem);
