import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Typography,
  Button,
  withStyles
} from "material-ui";
import PropTypes from "prop-types";

const LoginFailDialog = props => {
  const { classes, opened, toggleHandler } = props;
  return (
    <Dialog open={opened} onClose={toggleHandler}>
      <DialogTitle color="primary" id="dialog-title">
        Login failure
      </DialogTitle>
      <DialogContent>
        <Typography variant="subheading" id="dialog-description">
          The credentials you provided were incorrect.
        </Typography>
      </DialogContent>
      <DialogActions>
        <Button onClick={() => toggleHandler()}>OK</Button>
      </DialogActions>
    </Dialog>
  );
};

LoginFailDialog.propTypes = {
  classes: PropTypes.object,
  opened: PropTypes.bool,
  closeHandler: PropTypes.func
};
const styles = {};

export default withStyles(styles)(LoginFailDialog);
