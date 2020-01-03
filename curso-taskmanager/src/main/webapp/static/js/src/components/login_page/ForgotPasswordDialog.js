import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  Typography,
  Button,
  withStyles
} from "material-ui";
import PropTypes from "prop-types";

const ForgotPasswordDialog = props => {
  const { classes, opened, toggleHandler } = props;
  return (
    <Dialog
      aria-labelledby="dialog-title"
      aria-describedby="dialog-description"
      open={opened}
      onClose={toggleHandler}
    >
      <DialogTitle color="primary" id="dialog-title">
        Forgotten password
      </DialogTitle>
      <DialogContent>
        <Typography variant="subheading" id="dialog-description">
          To gain access or to reset password please contact administrator on
          email admin@blahobyty.cz
        </Typography>
      </DialogContent>
      <Button onClick={() => toggleHandler()}>OK</Button>
    </Dialog>
  );
};

ForgotPasswordDialog.propTypes = {
  classes: PropTypes.object,
  opened: PropTypes.bool,
  closeHandler: PropTypes.func
};
const styles = {};

export default withStyles(styles)(ForgotPasswordDialog);
