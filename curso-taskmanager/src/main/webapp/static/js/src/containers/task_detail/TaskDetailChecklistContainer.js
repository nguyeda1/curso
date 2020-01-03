import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import {
  taskDetailChecklistSelectors,
  taskDetailChecklistOperations
} from "es6!src/ducks/task_detail/checklist/index";
import { Button } from "material-ui";
import { bindActionCreators } from "redux";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import Panel from "es6!src/components/styled_components/Panel";
import TaskChecklistItemContainer from "es6!src/containers/task_checklist_item/TaskChecklistItemContainer";
import FixAboveNav from "es6!src/components/styled_components/FixAboveNav";

class TaskDetailChecklistContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { operations, taskId } = this.props;
    operations.fetchListByTaskId(taskId);
  }

  handleFinish(id) {
    const { operations } = this.props;
    operations.finishTask(id);
  }

  render() {
    const {
      data,
      fetched,
      taskId,
      noneUnbegun,
      isMine,
      taskFinished,
      finishHandler
    } = this.props;
    const list = data.map((c, index) => {
      return (
        <TaskChecklistItemContainer
          disabled={!isMine || taskFinished}
          key={c.id}
          data={c}
        />
      );
    });
    const finishButton =
      noneUnbegun && isMine && !taskFinished ? (
        <Button
          onClick={() => finishHandler()}
          variant="contained"
          color="primary"
          fullWidth
        >
          Finish task
        </Button>
      ) : null;
    return (
      <Panel>
        <ScrollableContent>{list}</ScrollableContent>
        <FixAboveNav>{finishButton}</FixAboveNav>
      </Panel>
    );
  }
}

TaskDetailChecklistContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  data: PropTypes.array.isRequired,
  fetched: PropTypes.bool
};

const mapStateToProps = state => {
  const {
    checklistSelector,
    fetchedSelector,
    noneUnbegunSelector,
    isMineSelector,
    taskFinishedSelector
  } = taskDetailChecklistSelectors;
  return {
    data: checklistSelector(state),
    noneUnbegun: noneUnbegunSelector(state),
    fetched: fetchedSelector(state),
    isMine: isMineSelector(state),
    taskFinished: taskFinishedSelector(state)
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, taskDetailChecklistOperations),
    dispatch
  )
});

export default connect(mapStateToProps, mapDispatchToProps, null, {
  withRef: true
})(TaskDetailChecklistContainer);
