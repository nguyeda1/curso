import React from "react";
import {
  taskListSelectors,
  taskListOperations
} from "es6!src/ducks/task_list/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import TaskListHeader from "es6!src/components/task_list/TaskListHeader";
import TaskListItem from "es6!src/components/task_list/TaskListItem";
import GroupByDate from "es6!src/components/task_list/GroupByDate";
import GroupByLocality from "es6!src/components/task_list/GroupByLocality";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import groupByProperty, {
  groupByObject
} from "es6!src/helpers/groupByProperty";
import withAsyncSearch from "es6!src/helpers/withAsyncSearch";
import TaskListFilter from "es6!src/components/task_list/TaskListFilter";
import TaskFinishDialog from "es6!src/components/task_finish/TaskFinishDialog";

class TaskListContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = { finishId: undefined };
  }

  componentDidMount() {
    const { operations, tab } = this.props;
    operations.fetchTasks(tab === "DONE", 0, 10);
  }

  handleShowDetail(value) {
    const { operations, match } = this.props;
    const path = `/task/view/${value}`;
    operations.redirect(path);
  }

  handleFinish(id) {
    const { operations } = this.props;
    this.setState({ finishId: id });
  }

  handleClose() {
    this.setState({ finishId: undefined });
  }

  handleSelect(value, problemText) {
    const { operations } = this.props;
    const { finishId } = this.state;

    operations.finishTask(finishId, value, problemText);
    this.handleClose();
  }

  handleFetchNext() {
    const { operations, page, fetched } = this.props;
    const pageSize = 10;
    operations.fetchNext(page, pageSize);
  }

  handleTabSwitch(event, value) {
    const { operations } = this.props;
    const done = value === "DONE";
    const page = done ? 0 : null;
    const max = done ? 10 : null;
    operations.fetchTasks(done, page, max);
    operations.switchTab(value);
  }

  render() {
    const {
      groups,
      size,
      fetched,
      tab,
      moreToLoad,
      renderAsyncScroll,
      searchBar,
      tasks
    } = this.props;

    const { finishId } = this.state;
    const inProgress = tab === "IN_PROGRESS";
    const finished = tab === "DONE";
    const groupedList =
      fetched && inProgress
        ? groups.map(group => {
            var localityGroups = [];
            if (!group.name) {
              group.name = "TBD";
            }
            localityGroups = groupByProperty(
              group.data,
              localityGroups,
              "locality"
            );
            return (
              <GroupByDate key={group.name} data={group.name}>
                {localityGroups.map(lg => {
                  return (
                    <GroupByLocality
                      key={group.name + "_" + lg.name}
                      data={lg.name}
                    >
                      {lg.data.map(task => {
                        return (
                          <TaskListItem
                            key={task.id}
                            onClick={t => this.handleShowDetail(t.id)}
                            finishTask={this.handleFinish.bind(this)}
                            data={task}
                          />
                        );
                      })}
                    </GroupByLocality>
                  );
                })}
              </GroupByDate>
            );
          })
        : null;

    const scrollProps = {
      data: tasks,
      fetch: () => (moreToLoad && fetched ? this.handleFetchNext() : null),
      hasMore: moreToLoad,
      renderComponent: TaskListItem,
      componentProps: { onClick: task => this.handleShowDetail(task.id) }
    };

    const finishedTasks = finished ? renderAsyncScroll(scrollProps) : null;

    const dialog = finishId ? (
      <TaskFinishDialog
        states={["DONE", "PARTIALLY", "NOT_DONE", "NO_TIME"]}
        selectHandler={this.handleSelect.bind(this)}
        open={finishId ? true : false}
        onClose={this.handleClose.bind(this)}
      />
    ) : null;
    return (
      <Panel>
        <TaskListHeader size={size} />
        <TaskListFilter
          value={tab}
          selectHandler={this.handleTabSwitch.bind(this)}
        />
        {finished && searchBar}
        <ScrollableContent>
          {inProgress && groupedList}
          {finished && finishedTasks}
        </ScrollableContent>
        {dialog}
      </Panel>
    );
  }
}

const mapStateToProps = state => {
  const {
    filteredTasksSelector,
    groupByListingSelector,
    taskListSizeSelector,
    fetchedSelector,
    tabSelector,
    moreToLoadSelector,
    pageSelector
  } = taskListSelectors;
  return {
    tab: tabSelector(state),
    tasks: filteredTasksSelector(state),
    groups: groupByListingSelector(state),
    size: taskListSizeSelector(state),
    fetched: fetchedSelector(state),
    moreToLoad: moreToLoadSelector(state),
    page: pageSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, taskListOperations, navigationOperations),
    dispatch
  )
});

TaskListContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  tasks: PropTypes.array.isRequired,
  groups: PropTypes.array.isRequired,
  fetched: PropTypes.bool,
  size: PropTypes.number
};

export default withAsyncSearch({
  elementId: "taskQuery",
  placeholder: "Start typing name...",
  search: query => taskListOperations.asyncSearch(query),
  defaultFetch: () => taskListOperations.fetchTasks(true, 0, 10),
  searchById: id => taskListOperations.searchById(id)
})(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(TaskListContainer)
);
