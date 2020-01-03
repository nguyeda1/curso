import React from "react";
import { Avatar, ListItemText, Typography, withStyles } from "material-ui";
import Item from "es6!src/components/styled_components/Item";
import moment from "moment";
import PropTypes from "prop-types";
import CheckBox from "es6!src/components/styled_components/CheckBox";

const TagListItem = props => {
  const { classes, data, caseId, checkHandler, checked } = props;

  const tag = (
    <Item>
      <ListItemText>
        <Typography variant="body1">{data.name}</Typography>
      </ListItemText>
      <CheckBox checked={checked} onChange={() => checkHandler(data)} />
    </Item>
  );

  return tag;
};

const styles = {
  avatar: {
    borderRadius: 10,
    width: 30,
    height: 30
  }
};

TagListItem.propTypes = {
  data: PropTypes.object.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(TagListItem);
