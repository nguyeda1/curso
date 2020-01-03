import React from "react";
import { Toolbar, Icon, withStyles } from "material-ui";
import PropTypes from "prop-types";
import RightHeaderButton from "es6!src/components/styled_components/RightHeaderButton";
import LeftHeaderButton from "es6!src/components/styled_components/LeftHeaderButton";
import HeaderTitle from "es6!src/components/styled_components/HeaderTitle";

const CaseListHeader = props => {
  const { classes, createCaseHandler } = props;
  return (
    <Toolbar>
      <HeaderTitle>Cases</HeaderTitle>
      <RightHeaderButton onClick={() => createCaseHandler()}>
        <Icon>add</Icon>
      </RightHeaderButton>
    </Toolbar>
  );
};

const styles = {};

CaseListHeader.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(CaseListHeader);
