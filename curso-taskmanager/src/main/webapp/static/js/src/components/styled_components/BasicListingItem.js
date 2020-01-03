import React from "react";
import { Typography, Icon, withStyles } from "material-ui";
import PropTypes from "prop-types";
import RightHeaderButton from "es6!src/components/styled_components/RightHeaderButton";
import LeftHeaderButton from "es6!src/components/styled_components/LeftHeaderButton";
import BasicItem from "es6!src/components/styled_components/BasicItem";
import LinkButton from "es6!src/components/styled_components/LinkButton";

const BasicListingItem = props => {
  const { classes, data, onClick, ...other } = props;
  return (
    <BasicItem {...other} avatar="L">
      <LinkButton onClick={onClick}>{data.name}</LinkButton>
      <Typography variant="caption">{data.locality}</Typography>
    </BasicItem>
  );
};

const styles = {};

BasicListingItem.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(BasicListingItem);
