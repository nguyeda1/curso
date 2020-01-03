import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import TaskChecklistItem from "es6!src/components/task_checklist/TaskChecklistItem";
import { taskDetailChecklistOperations } from "es6!src/ducks/task_detail/checklist/index";

class TaskChecklistItemContainer extends React.Component {
  constructor(props) {
    super(props);
    const { state } = props;
    this.state = {
      value: "",
      openSelect: false
    };
  }

  isDone() {
    const { value } = this.state;
    return value === "DONE";
  }

  componentDidMount() {
    const { data } = this.props;
    this.setState({ value: data.state, openSelect: false });
  }

  handleSelectToggle(event) {
    this.setState({ openSelect: !this.state.openSelect });
  }

  handleSelectChange(event) {
    const { operations, data } = this.props;
    operations.changeState({ ...data, state: event.target.value });
    this.setState({ value: event.target.value });
  }

  render() {
    const { openSelect, value } = this.state;
    const { data, disabled } = this.props;
    return (
      <TaskChecklistItem
        {...this.props}
        value={value}
        opened={openSelect}
        onChange={this.handleSelectChange.bind(this)}
        onToggle={this.handleSelectToggle.bind(this)}
      />
    );
  }
}

TaskChecklistItemContainer.propTypes = {};

const mapStateToProps = state => {
  return {};
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, taskDetailChecklistOperations),
    dispatch
  )
});

export default connect(mapStateToProps, mapDispatchToProps, null, {
  withRef: true
})(TaskChecklistItemContainer);
