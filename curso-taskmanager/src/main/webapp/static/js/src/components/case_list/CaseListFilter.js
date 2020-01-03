import React from "react";
import { Tabs, Tab, withStyles } from "material-ui";
import PropTypes from "prop-types";
import normalizeText from "es6!src/helpers/normalizeText";

const CaseListFilter = props => {
  const { classes, value, selectHandler, tabs } = props;
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
      {tabs.map(tab => {
        return (
          <Tab
            key={tab}
            value={tab}
            label={normalizeText(tab)}
            classes={{ root: classes.tabRoot }}
          />
        );
      })}
    </Tabs>
  );
};

const styles = {
  tabRoot: {
    textTransform: "initial"
  }
};

CaseListFilter.propTypes = {
  tabs: PropTypes.array.isRequired,
  selectHandler: PropTypes.func,
  classes: PropTypes.object
};

export default withStyles(styles)(CaseListFilter);
