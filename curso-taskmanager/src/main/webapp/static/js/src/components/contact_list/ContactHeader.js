import React from "react";
import { Toolbar, withStyles } from "material-ui";
import PropTypes from "prop-types";
import HeaderTitle from "es6!src/components/styled_components/HeaderTitle";

const ContactHeader = props => {
  const { classes } = props;
  return (
    <Toolbar className={classes.parent}>
      <HeaderTitle variant="subheading">
        <strong>Contacts</strong>
      </HeaderTitle>
    </Toolbar>
  );
};

const styles = { parent: { alignItems: "flex-end" } };

ContactHeader.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(ContactHeader);
