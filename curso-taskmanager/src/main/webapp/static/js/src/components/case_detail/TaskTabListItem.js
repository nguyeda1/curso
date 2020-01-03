import React from "react";
import { Typography, ListItem, ListItemText, withStyles } from "material-ui";
import PropTypes from "prop-types";
import { getCurrentUser } from "es6!src/helpers/localStorage";
import CheckBox from "es6!src/components/styled_components/CheckBox";
import LinkButton from "es6!src/components/styled_components/LinkButton";

const TaskTabListItem = props => {
  const { classes, data, assignHandler, onClick } = props;

  const assignedTo = data.assignee.id ? (
    <Typography variant="caption">{data.assignee.fullname}</Typography>
  ) : null;

  const assignButton = data.assignee ? null : (
    <LinkButton onClick={() => assignHandler(data.id)}>Assign to me</LinkButton>
  );

  return (
    <div className={classes.container}>
      <ListItem button onClick={onClick} className={classes.item}>
        <ListItemText>
          <Typography variant="body1">{data.name}</Typography>
          {assignedTo}
        </ListItemText>
      </ListItem>
      {assignButton}
    </div>
  );
};

const styles = {
  container: { display: "flex", alignItems: "center", paddingRight: "15px" },
  item: {
    height: 50
  }
};

TaskTabListItem.propTypes = {
  data: PropTypes.object,
  checked: PropTypes.bool,
  handleCheck: PropTypes.func,
  classes: PropTypes.object
};

export default withStyles(styles)(TaskTabListItem);
