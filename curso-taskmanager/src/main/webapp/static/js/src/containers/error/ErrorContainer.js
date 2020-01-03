import React from "react";

import { errorSelectors, errorOperations } from "es6!src/ducks/error/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import {
  withStyles,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button
} from "material-ui";

class ErrorContainer extends React.Component {
  constructor(props, context) {
    super(props);
  }

  handleClose() {
    const { operations } = this.props;
    operations.close();
  }

  render() {
    const { code, text } = this.props;
    const dialog = (
      <Dialog open={code ? true : false} onClose={this.handleClose.bind(this)}>
        <DialogTitle id="alert-dialog-title">Error {code}</DialogTitle>
        <DialogContent>
          <DialogContentText>{text}</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={this.handleClose.bind(this)}
            color="primary"
            autoFocus
          >
            Close
          </Button>
        </DialogActions>
      </Dialog>
    );

    return dialog;
  }
}
const styles = {
  app: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between"
  }
};

const mapStateToProps = state => {
  const { errorCodeSelector, errorTextSelector } = errorSelectors;
  return {
    code: errorCodeSelector(state),
    text: errorTextSelector(state)
    // lastFetch: lastFetchSelector(state)
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators({ ...errorOperations }, dispatch)
});

export default withStyles(styles)(
  connect(mapStateToProps, mapDispatchToProps)(ErrorContainer)
);
