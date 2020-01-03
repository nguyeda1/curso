import React from "react";
import { List, ListSubheader, withStyles } from "material-ui";
import PropTypes from "prop-types";

const TaskTabGroup = props => {
  const { classes, data, children } = props;

  return (
    <div>
      <ListSubheader
        disableSticky
        className={classes[data.name]}
        component="div"
      >
        {data.name}
      </ListSubheader>
      {children}
    </div>
  );
};

const styles = {
  header: { backgroundColor: "#e5e5e5" },
  LOW: { color: "green" },
  MEDIUM: { color: "orange" },
  HIGH: { color: "red" }
};

TaskTabGroup.propTypes = {
  data: PropTypes.object.isRequired,
  children: PropTypes.node,
  classes: PropTypes.object
};

export default withStyles(styles)(TaskTabGroup);
