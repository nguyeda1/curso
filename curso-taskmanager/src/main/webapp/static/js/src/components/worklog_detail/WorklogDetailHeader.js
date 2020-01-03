import React from "react";
import { Toolbar, Icon, withStyles } from "material-ui";
import PropTypes from "prop-types";
import RightHeaderButton from "es6!src/components/styled_components/RightHeaderButton";
import LeftHeaderButton from "es6!src/components/styled_components/LeftHeaderButton";
import HeaderTitle from "es6!src/components/styled_components/HeaderTitle";

const CaseDetailHeader = props => {
  const { classes, backHandler, editHandler, editable } = props;
  return (
    <Toolbar>
      <LeftHeaderButton onClick={() => backHandler()}>
        <Icon>keyboard_arrow_left</Icon>
      </LeftHeaderButton>
      <HeaderTitle className={classes.header} variant="title">
        Worklog detail
      </HeaderTitle>
      {editable ? (
        <RightHeaderButton onClick={() => editHandler()}>
          <Icon>edit</Icon>
        </RightHeaderButton>
      ) : null}
    </Toolbar>
  );
};

const styles = {};

CaseDetailHeader.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(CaseDetailHeader);
