import React from "react";
import {
  SnackbarContent,
  withStyles,
  Typography,
  ListItemText,
  Button,
  Avatar,
  ListItem,
  MenuItem,
  Select
} from "material-ui";
import Item from "es6!src/components/styled_components/Item";
import PropTypes from "prop-types";
import moment from "moment";
import normalizeText from "es6!src/helpers/normalizeText";

const TaskChecklistItem = props => {
  const { data, classes, onChange, onToggle, opened, value, disabled } = props;
  return (
    <ListItem>
      <ListItemText>
        <Typography variant="body1">{data.name}</Typography>
      </ListItemText>
      <Select
        id="state_select"
        open={opened}
        onClose={onToggle}
        onOpen={onToggle}
        value={value}
        onChange={onChange}
        disabled={disabled}
      >
        <MenuItem value={"UNBEGUN"}>Unbegun</MenuItem>
        <MenuItem value={"FAIL"}>Fail</MenuItem>
        <MenuItem value={"DONE"}>Done</MenuItem>
      </Select>
    </ListItem>
  );
};

const styles = { hiddenInput: { display: "none" } };

TaskChecklistItem.propTypes = {
  data: PropTypes.object.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(TaskChecklistItem);
