import React from "react";
import { Toolbar, Icon, withStyles } from "material-ui";
import PropTypes from "prop-types";
import RightHeaderButton from "es6!src/components/styled_components/RightHeaderButton";
import LeftHeaderButton from "es6!src/components/styled_components/LeftHeaderButton";
import HeaderTitle from "es6!src/components/styled_components/HeaderTitle";

const CaseEditHeader = props => {
  const { classes, backHandler, existing, submitHandler } = props;
  return (
    <Toolbar>
      <LeftHeaderButton onClick={() => backHandler()}>
        <Icon>keyboard_arrow_left</Icon>
      </LeftHeaderButton>
      <HeaderTitle className={classes.header} variant="title">
        {existing ? "Edit case" : "New case"}
      </HeaderTitle>
      <RightHeaderButton onClick={() => submitHandler()}>
        <Icon>check</Icon>
      </RightHeaderButton>
    </Toolbar>
  );
};

const styles = {};

CaseEditHeader.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(CaseEditHeader);
