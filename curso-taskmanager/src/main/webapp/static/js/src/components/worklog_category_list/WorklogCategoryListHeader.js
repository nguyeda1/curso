import React from "react";
import { Toolbar, Icon, withStyles } from "material-ui";
import PropTypes from "prop-types";
import RightHeaderButton from "es6!src/components/styled_components/RightHeaderButton";
import LeftHeaderButton from "es6!src/components/styled_components/LeftHeaderButton";
import HeaderTitle from "es6!src/components/styled_components/HeaderTitle";

const WorklogCategoryListHeader = props => {
  const { classes, backHandler, submitHandler } = props;
  return (
    <Toolbar>
      <LeftHeaderButton onClick={() => backHandler()}>
        <Icon>keyboard_arrow_left</Icon>
      </LeftHeaderButton>
      <HeaderTitle className={classes.header} variant="title">
        Worklog category
      </HeaderTitle>
    </Toolbar>
  );
};

const styles = {};

WorklogCategoryListHeader.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(WorklogCategoryListHeader);
