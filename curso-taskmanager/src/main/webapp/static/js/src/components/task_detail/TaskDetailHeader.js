import React from "react";
import { Toolbar, Icon, Button, withStyles } from "material-ui";
import PropTypes from "prop-types";
import RightHeaderButton from "es6!src/components/styled_components/RightHeaderButton";
import LeftHeaderButton from "es6!src/components/styled_components/LeftHeaderButton";
import HeaderTitle from "es6!src/components/styled_components/HeaderTitle";

const TaskDetailHeader = props => {
  const { classes, backHandler, renderRightButton } = props;
  return (
    <Toolbar className={classes.noPaddingRight}>
      <LeftHeaderButton onClick={() => backHandler()}>
        <Icon>keyboard_arrow_left</Icon>
      </LeftHeaderButton>
      <HeaderTitle className={classes.header} variant="title">
        Task detail
      </HeaderTitle>
      {renderRightButton ? renderRightButton : null}
    </Toolbar>
  );
};

const styles = { noPaddingRight: { paddingRight: 0 } };

TaskDetailHeader.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(TaskDetailHeader);
