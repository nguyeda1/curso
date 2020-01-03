import React from "react";
import { withStyles } from "material-ui";
import PropTypes from "prop-types";
import ActivityListItem from "es6!src/components/activity_list/ActivityListItem";

const CaseDetailHistoryTab = props => {
  const { data, classes } = props;
  const history = (
    <div>
      {data.map(item => {
        return <ActivityListItem key={item.id} data={item} />;
      })}
    </div>
  );
  return history;
};

const styles = theme => ({});

CaseDetailHistoryTab.propTypes = {
  classes: PropTypes.object,
  data: PropTypes.array
};

export default withStyles(styles)(CaseDetailHistoryTab);
