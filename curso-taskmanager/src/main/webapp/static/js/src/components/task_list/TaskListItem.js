import React from "react";
import {
  ListItem,
  ListItemText,
  Button,
  withStyles,
  Typography
} from "material-ui";
import PropTypes from "prop-types";
import CheckBox from "es6!src/components/styled_components/CheckBox";
import Priority from "es6!src/components/styled_components/Priority";
import StateTag from "es6!src/components/styled_components/StateTag";

const TaskListItem = props => {
  const { classes, data, onClick, finishTask } = props;

  const finishButton =
    data.checklistDone && !data.finished ? (
      <Button
        onClick={() => finishTask(data.id)}
        size="small"
        color="primary"
        variant="contained"
      >
        Finish
      </Button>
    ) : null;
  const { cursoCase, listing } = data;

  const listingName = listing ? listing.name : null;
  const priority = data.priority ? <Priority>{data.priority}</Priority> : null;
  const state = data.finishedState ? (
    <StateTag>{data.finishedState}</StateTag>
  ) : null;
  return (
    <div className={classes.container}>
      <ListItem button onClick={() => onClick(data)}>
        <ListItemText>
          <Typography className={classes.header} variant="body1">
            {priority}&nbsp;
            {data.priorityOnScreen ? (
              <strong>({data.priorityOnScreen})</strong>
            ) : (
              ""
            )}
            &nbsp;
            {data.name}
          </Typography>
          <Typography variant="caption">{listingName}</Typography>
        </ListItemText>
      </ListItem>
      {finishButton}
      {state}
    </div>
  );
};

const styles = {
  header: {
    display: "inline-flex",
    alignItems: "flex-end"
  },
  container: {
    display: "flex",
    alignItems: "center",
    paddingRight: "15px"
  }
};

TaskListItem.propTypes = {
  data: PropTypes.object,
  checked: PropTypes.bool,
  handleCheck: PropTypes.func,
  classes: PropTypes.object
};

export default withStyles(styles)(TaskListItem);
