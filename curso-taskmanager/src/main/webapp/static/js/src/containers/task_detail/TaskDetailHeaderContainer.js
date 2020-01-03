import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import {
  taskDetailHeaderSelectors,
  taskDetailHeaderOperations
} from "es6!src/ducks/task_detail/header/index";
import { taskDetailChecklistOperations } from "es6!src/ducks/task_detail/checklist/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import { getCurrentUser } from "es6!src/helpers/localStorage";
import TaskDetailHeader from "es6!src/components/task_detail/TaskDetailHeader";
import { withRouter } from "react-router-dom";
import RightHeaderButton from "es6!src/components/styled_components/RightHeaderButton";

class TaskDetailHeaderContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  handleBack() {
    const { history, operations } = this.props;
    history.goBack();
  }

  handleAssign() {
    const { operations, taskId } = this.props;
    operations.assignToMe(taskId);
  }

  render() {
    const {
      fetched,
      selectedTab,
      match,
      assignee,
      disabled,
      checkListDone,
      finishHandler
    } = this.props;

    const allDone = checkListDone && assignee.id;
    const isMine = assignee.id === getCurrentUser().id;

    const rightButton = disabled ? null : (
      <RightHeaderButton
        label
        onClick={() =>
          allDone && isMine
            ? finishHandler()
            : !assignee.id ? this.handleAssign() : null
        }
      >
        {allDone && isMine ? "FINISH" : !assignee.id ? "assign to me" : null}
      </RightHeaderButton>
    );
    return (
      <TaskDetailHeader
        backHandler={this.handleBack.bind(this)}
        renderRightButton={rightButton}
        assignHandler={this.handleAssign.bind(this)}
      />
    );
  }
}

const mapStateToProps = state => {
  const {
    assigneeSelector,
    disabledSelector,
    checklistDoneSelector
  } = taskDetailHeaderSelectors;
  return {
    assignee: assigneeSelector(state),
    disabled: disabledSelector(state),
    checkListDone: checklistDoneSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign(
      {},
      taskDetailHeaderOperations,
      taskDetailChecklistOperations,
      navigationOperations
    ),
    dispatch
  )
});

TaskDetailHeaderContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  fetched: PropTypes.bool
};

export default connect(mapStateToProps, mapDispatchToProps)(
  withRouter(TaskDetailHeaderContainer)
);
