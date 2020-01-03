import React from "react";
import { Tabs, Tab, withStyles, Badge } from "material-ui";
import PropTypes from "prop-types";
const CaseDetailTabPanel = props => {
  const { classes, value, selectHandler, selected, tasksAmount } = props;
  return (
    <Tabs
      value={selected}
      onChange={selectHandler}
      indicatorColor="primary"
      textColor="primary"
      scrollable
      scrollButtons="off"
      fullWidth
    >
      <Tab
        key="Case"
        value="Case"
        label="Case"
        classes={{ root: classes.tabRoot }}
      />
      <Tab
        key="Tasks"
        value="Tasks"
        label={
          <Badge
            classes={{ badge: classes.padding }}
            color="secondary"
            badgeContent={tasksAmount}
          >
            Tasks
          </Badge>
        }
        classes={{ root: classes.tabRoot }}
      />
      <Tab
        key="History"
        value="History"
        label="History"
        classes={{ root: classes.tabRoot }}
      />
      <Tab
        key="Comments"
        value="Comments"
        label="Comments"
        classes={{ root: classes.tabRoot }}
      />
    </Tabs>
  );
};

const styles = theme => ({
  tabRoot: {
    textTransform: "initial"
  },
  padding: { top: "-16px", right: "-18px" }
});

CaseDetailTabPanel.propTypes = {
  selectHandler: PropTypes.func,
  classes: PropTypes.object
};

export default withStyles(styles)(CaseDetailTabPanel);
