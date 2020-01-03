import React from "react";
import { Toolbar, Icon, withStyles } from "material-ui";
import PropTypes from "prop-types";
import RightHeaderButton from "es6!src/components/styled_components/RightHeaderButton";
import LeftHeaderButton from "es6!src/components/styled_components/LeftHeaderButton";
import HeaderTitle from "es6!src/components/styled_components/HeaderTitle";

const BookingListHeader = props => {
  const { classes, backHandler } = props;
  return (
    <Toolbar>
      <LeftHeaderButton onClick={() => backHandler("form")}>
        <Icon>keyboard_arrow_left</Icon>
      </LeftHeaderButton>
      <HeaderTitle className={classes.header} variant="title">
        Booking
      </HeaderTitle>
    </Toolbar>
  );
};

const styles = {};

BookingListHeader.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(BookingListHeader);
