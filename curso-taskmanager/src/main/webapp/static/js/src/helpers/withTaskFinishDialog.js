import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import TaskFinishDialog from "es6!src/components/task_finish/TaskFinishDialog";

const withTaskFinishDialog = ({ finish }) => id => Component => {
  class WithTaskFinishDialog extends React.Component {
    constructor(props) {
      super(props);
      this.state = { open: props.opened };
    }

    handleClose() {
      this.setState({ open: false });
    }

    handleSelect(value) {
      const { actions } = this.props;
      actions.finish(id, value);
      this.handleClose();
    }

    render() {
      const dialog = (
        <TaskFinishDialog
          states={["DONE", "PARTIALLY", "NOT_DONE", "NO_TIME"]}
          onClose={this.handleClose.bind(this)}
          selectHandler={this.handleSelect.bind(this)}
        />
      );
      return <Component {...this.props} renderFinishDialog={dialog} />;
    }
  }

  const mapStateToProps = () => {
    return (state, ownProps) => {
      return { opened: ownProps.opened };
    };
  };

  const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(
      {
        finish
      },
      dispatch
    )
  });

  return connect(mapStateToProps, mapDispatchToProps)(WithTaskFinishDialog);

  WithTaskFinishDialog.propTypes = {};
};
export default withTaskFinishDialog;
