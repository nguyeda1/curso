import React from "react";
import {
  List,
  ExpansionPanel,
  ExpansionPanelSummary,
  ExpansionPanelDetails,
  Icon,
  Typography,
  withStyles
} from "material-ui";
import PropTypes from "prop-types";
import moment from "moment";
import ListingItem from "es6!src/components/listing_list/ListingItem";
import groupByProperty from "es6!src/helpers/groupByProperty";

const GroupByDate = props => {
  const { classes, data, children } = props;
  const isTBD = data === "TBD";
  if (!isTBD) {
    const date = moment(data);
    const daysFromToday = date.diff(moment().startOf("day"), "days");

    const isToday = daysFromToday === 0;
    const isTomorrow = daysFromToday === 1;
    const wasYesterday = daysFromToday === -1;
    const inFuture = daysFromToday > 1;
    const inPast = daysFromToday < -1;
    const expand = (
      <ExpansionPanel defaultExpanded>
        <ExpansionPanelSummary
          className={
            isToday || isTomorrow
              ? classes.infoHeader
              : wasYesterday || inPast ? classes.alertHeader : classes.okHeader
          }
          expandIcon={<Icon>expand_more</Icon>}
        >
          <Typography variant="subheading">
            <strong>
              {isToday && "Today"}
              {isTomorrow && "Tomorrow"}
              {wasYesterday && "Yesterday"}
              {inFuture && date.format("Do MMMM YYYY")}
              {inPast && date.format("Do MMMM YYYY")}
            </strong>
          </Typography>
        </ExpansionPanelSummary>

        {children}
      </ExpansionPanel>
    );

    return expand;
  } else {
    const expand = (
      <ExpansionPanel defaultExpanded>
        <ExpansionPanelSummary
          className={classes.okHeader}
          expandIcon={<Icon>expand_more</Icon>}
        >
          <Typography variant="subheading">
            <strong>{isTBD && "TBD"}</strong>
          </Typography>
        </ExpansionPanelSummary>

        {children}
      </ExpansionPanel>
    );

    return expand;
  }
};

const styles = {
  okHeader: { paddingLeft: "10px", backgroundColor: "#2dc340" },
  infoHeader: { paddingLeft: "10px", backgroundColor: "#f4d436" },
  alertHeader: { paddingLeft: "10px", backgroundColor: "#f44336" }
};

GroupByDate.propTypes = {
  data: PropTypes.string,
  children: PropTypes.node,
  classes: PropTypes.object
};

export default withStyles(styles)(GroupByDate);
