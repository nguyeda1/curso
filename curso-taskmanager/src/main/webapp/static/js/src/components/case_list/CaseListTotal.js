import React from "react";
import { ListItem, Typography, withStyles } from "material-ui";
import PropTypes from "prop-types";

const CaseListTotal = props => {
  const { classes, data } = props;
  return (
    <ListItem>
      <Typography variant="caption">There are {data} cases</Typography>
    </ListItem>
  );
};

const styles = {};

CaseListTotal.propTypes = {
  data: PropTypes.number.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(CaseListTotal);
