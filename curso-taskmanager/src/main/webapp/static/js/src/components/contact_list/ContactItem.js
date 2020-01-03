import React from "react";
import {
  ListItem,
  ListItemText,
  Typography,
  IconButton,
  Icon,
  withStyles
} from "material-ui";
import normalizeText from "es6!src/helpers/normalizeText";
import PropTypes from "prop-types";

const ContactItem = props => {
  const { classes, data } = props;
  const type = data.type === "IT" ? data.type : normalizeText(data.type);
  const phone = data.user && data.user.phone;
  return (
    <ListItem>
      <ListItemText>
        <Typography variant="body1">{data.user && data.user.fullname}</Typography>
        <Typography variant="caption">
          <strong>{type}</strong>
        </Typography>
        <Typography variant="caption">{phone}</Typography>
      </ListItemText>
      <IconButton href={"tel:" + phone} className={classes.call} variant="fab">
        <Icon>call</Icon>
      </IconButton>
    </ListItem>
  );
};

const styles = {
  call: {
    color: "white",
    backgroundColor: "red",
    width: 40,
    height: 40
  }
};

ContactItem.propTypes = {
  data: PropTypes.object.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(ContactItem);
