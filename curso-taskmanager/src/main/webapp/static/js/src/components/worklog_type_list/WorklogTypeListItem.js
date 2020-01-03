import React from "react";
import { Avatar, ListItemText, Typography, withStyles } from "material-ui";
import Item from "es6!src/components/styled_components/Item";
import moment from "moment";
import PropTypes from "prop-types";
import CheckBox from "es6!src/components/styled_components/CheckBox";

const WorklogTypeListItem = props => {
  const { classes, data, caseId, selectHandler, checked } = props;

  const tag = (
    <Item onClick={() => selectHandler("type", data)}>
      <ListItemText>
        <Typography variant="body1">{data}</Typography>
      </ListItemText>
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

WorklogTypeListItem.propTypes = {
  data: PropTypes.object.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(WorklogTypeListItem);
