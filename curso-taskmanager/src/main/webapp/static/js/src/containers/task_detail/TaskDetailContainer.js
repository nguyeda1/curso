import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import { taskDetailTabPanelSelectors } from "es6!src/ducks/task_detail/tab_panel/index";
import { taskDetailChecklistOperations } from "es6!src/ducks/task_detail/checklist/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import Page from "es6!src/components/styled_components/Page";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import TaskDetailTabPanelContainer from "es6!src/containers/task_detail/TaskDetailTabPanelContainer";
import TaskDetailGeneralContainer from "es6!src/containers/task_detail/TaskDetailGeneralContainer";
import TaskDetailWorklogContainer from "es6!src/containers/task_detail/TaskDetailWorklogContainer";
import TaskDetailChecklistContainer from "es6!src/containers/task_detail/TaskDetailChecklistContainer";
import TaskDetailCommentsContainer from "es6!src/containers/task_detail/TaskDetailCommentsContainer";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import TaskDetailHeaderContainer from "es6!src/containers/task_detail/TaskDetailHeaderContainer";
import StyledInput from "es6!src/components/styled_components/StyledInput";
import TaskFinishDialog from "es6!src/components/task_finish/TaskFinishDialog";

class TaskDetailContainer extends React.Component {
  constructor(props) {
    super(props);
    const { match } = this.props;
    const id = match.params.id;
    this.state = { id, selectedTab: "General", finishDialog: false };
  }

  handlSwitchTab(selectedTab) {
    this.setState({ selectedTab });
  }

  handleFinish() {
    this.setState({ finishDialog: true });
  }

  handleClose() {
    this.setState({ finishDialog: false });
  }

  handleSelect(value, problemText) {
    const { operations } = this.props;
    const { id } = this.state;
    operations.finishTask(id, value, problemText);
    this.handleClose();
  }

  render() {
    const { fetched } = this.props;
    const { id, selectedTab, finishDialog } = this.state;

    const dialog = finishDialog ? (
      <TaskFinishDialog
        states={["DONE", "PARTIALLY", "NOT_DONE", "NO_TIME"]}
        selectHandler={this.handleSelect.bind(this)}
        open={finishDialog}
        onClose={this.handleClose.bind(this)}
      />
    ) : null;
    return (
      <Page>
        <TaskDetailHeaderContainer
          finishHandler={this.handleFinish.bind(this)}
          taskId={id}
        />
        <TaskDetailTabPanelContainer
          switchTab={this.handlSwitchTab.bind(this)}
          selectedTab={selectedTab}
        />
        {selectedTab === "General" && (
          <TaskDetailGeneralContainer taskId={id} />
        )}
        {selectedTab === "Checklist" && (
          <TaskDetailChecklistContainer
            finishHandler={this.handleFinish.bind(this)}
            taskId={id}
          />
        )}
        {selectedTab === "Comments" && (
          <TaskDetailCommentsContainer taskId={id} />
        )}
        {dialog}
      </Page>
    );
  }
}

const mapStateToProps = state => {
  const { selectedSelector } = taskDetailTabPanelSelectors;
  return {
    selectedTab: selectedSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, navigationOperations, taskDetailChecklistOperations),
    dispatch
  )
});

TaskDetailContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  fetched: PropTypes.bool
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TaskDetailContainer);
