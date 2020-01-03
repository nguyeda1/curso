import React from "react";
import { withStyles, Dialog, Slide } from "material-ui";
import PropTypes from "prop-types";

const SlideDialog = props => {
  const { classes, children } = props;

  return (
    <Dialog
      fullScreen
      TransitionComponent={props => <Slide direction="left" {...props} />}
      {...props}
    >
      {children}
    </Dialog>
  );
};

const styles = {};

SlideDialog.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(SlideDialog);
