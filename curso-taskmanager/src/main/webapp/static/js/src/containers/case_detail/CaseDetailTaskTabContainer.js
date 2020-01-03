import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";

import PropTypes from "prop-types";
import {
  caseDetailTaskListSelectors,
  caseDetailTaskListOperations
} from "es6!src/ducks/case_detail/tasks/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";

import TaskTabGroup from "es6!src/components/case_detail/TaskTabGroup";
import TaskTabListItem from "es6!src/components/case_detail/TaskTabListItem";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";

class CaseDetailTaskTabContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { operations, caseId, fetched } = this.props;
    operations.fetchListByCaseId(caseId);
  }

  handleAssign(id) {
    const { operations } = this.props;
    operations.assign(id);
  }

  handleShowDetail(value) {
    const { operations, match } = this.props;
    const path = `/task/view/${value}`;
    operations.redirect(path);
  }

  render() {
    const { groups } = this.props;
    const taskList = groups.map(group => {
      return (
        <TaskTabGroup key={group.name} data={group}>
          {group.data.map(task => {
            return (
              <TaskTabListItem
                key={task.id}
                assignHandler={() => this.handleAssign(task.id)}
                onClick={() => this.handleShowDetail(task.id)}
                data={task}
              />
            );
          })}
        </TaskTabGroup>
      );
    });

    return <ScrollableContent>{taskList}</ScrollableContent>;
  }
}

const mapStateToProps = state => {
  const {
    groupByPrioritySelector,
    fetchedSelector
  } = caseDetailTaskListSelectors;
  return {
    groups: groupByPrioritySelector(state),
    fetched: fetchedSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, caseDetailTaskListOperations, navigationOperations),
    dispatch
  )
});

CaseDetailTaskTabContainer.propTypes = {
  groups: PropTypes.array.isRequired,
  fetched: PropTypes.bool,
  moreToLoad: PropTypes.bool,
  total: PropTypes.number
};

export default connect(mapStateToProps, mapDispatchToProps)(
  CaseDetailTaskTabContainer
);
