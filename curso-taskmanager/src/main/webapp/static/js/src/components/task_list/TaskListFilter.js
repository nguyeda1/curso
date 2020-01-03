import React from "react";
import { Tabs, Tab, withStyles } from "material-ui";
import PropTypes from "prop-types";
import normalizeText from "es6!src/helpers/normalizeText";

const TaskListFilter = props => {
  const { classes, value, selectHandler } = props;
  return (
    <Tabs
      value={value}
      onChange={selectHandler}
      indicatorColor="primary"
      textColor="primary"
      scrollable
      scrollButtons="off"
      fullWidth
    >
      <Tab
        key={"IN_PROGRESS"}
        value={"IN_PROGRESS"}
        label={"In progress"}
        classes={{ root: classes.tabRoot }}
      />
      <Tab
        key={"DONE"}
        value={"DONE"}
        label={"Finished"}
        classes={{ root: classes.tabRoot }}
      />
    </Tabs>
  );
};

const styles = {
  tabRoot: {
    textTransform: "initial"
  }
};

TaskListFilter.propTypes = {
  selectHandler: PropTypes.func,
  classes: PropTypes.object
};

export default withStyles(styles)(TaskListFilter);
