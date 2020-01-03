import React from "react";
import { Typography, Icon, withStyles } from "material-ui";
import PropTypes from "prop-types";
import RightHeaderButton from "es6!src/components/styled_components/RightHeaderButton";
import LeftHeaderButton from "es6!src/components/styled_components/LeftHeaderButton";
import BasicItem from "es6!src/components/styled_components/BasicItem";
import LinkButton from "es6!src/components/styled_components/LinkButton";

const BasicCaseItem = props => {
  const { classes, data, onClick, ...other } = props;
  return (
    <BasicItem {...other} avatar="C">
      <LinkButton onClick={onClick}>{data.name}</LinkButton>
    </BasicItem>
  );
};

const styles = {};

BasicCaseItem.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(BasicCaseItem);
