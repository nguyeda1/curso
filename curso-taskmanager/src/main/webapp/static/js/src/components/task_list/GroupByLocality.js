import React from "react";
import { List, Typography, withStyles } from "material-ui";
import PropTypes from "prop-types";
import moment from "moment";
import ListingItem from "es6!src/components/listing_list/ListingItem";
import groupByProperty from "es6!src/helpers/groupByProperty";

const GroupByLocality = props => {
  const { classes, data, children } = props;
  return (
    <List>
      <div className={classes.header}>
        <Typography variant="body1">{data}</Typography>
      </div>
      {children}
    </List>
  );
};

const styles = { header: { paddingLeft: "10px", backgroundColor: "#e5e5e5" } };

GroupByLocality.propTypes = {
  data: PropTypes.string,
  children: PropTypes.node,
  classes: PropTypes.object
};

export default withStyles(styles)(GroupByLocality);
