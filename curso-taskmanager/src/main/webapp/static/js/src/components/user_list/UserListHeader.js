import React from "react";
import { Toolbar, Icon, withStyles } from "material-ui";
import PropTypes from "prop-types";
import RightHeaderButton from "es6!src/components/styled_components/RightHeaderButton";
import LeftHeaderButton from "es6!src/components/styled_components/LeftHeaderButton";
import HeaderTitle from "es6!src/components/styled_components/HeaderTitle";

const UserListHeader = props => {
  const { classes, backHandler } = props;
  return (
    <Toolbar>
      <LeftHeaderButton onClick={() => backHandler("form")}>
        <Icon>keyboard_arrow_left</Icon>
      </LeftHeaderButton>
      <HeaderTitle className={classes.header} variant="title">
        Owner
      </HeaderTitle>
    </Toolbar>
  );
};

const styles = {};

UserListHeader.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(UserListHeader);
