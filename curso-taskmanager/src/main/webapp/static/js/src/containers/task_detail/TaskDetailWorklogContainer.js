import React from "react";
import PropTypes from "prop-types";
import {
  worklogListSelectors,
  worklogListOperations
} from "es6!src/ducks/worklog_list/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import WorklogItem from "es6!src/components/worklog_list/WorklogItem";

import Page from "es6!src/components/styled_components/Page";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import { Button } from "material-ui";
import withAsyncSearch from "es6!src/helpers/withAsyncSearch";
import { Typography, ListItem, ListItemText } from "material-ui";
import SquareAvatar from "es6!src/components/styled_components/SquareAvatar";

class WorklogListContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { operations, page, taskId, fetched } = this.props;
    const pageSize = 10;
    operations.fetch(taskId, 0, pageSize);
  }

  fetchNext(e) {
    const { operations, page, fetched, taskId } = this.props;
    const pageSize = 10;
    const max = page > 0 ? pageSize * page : pageSize;
    operations.fetchNext(taskId, page, pageSize);
  }

  render() {
    const {
      renderAsyncScroll,
      data,
      fetched,
      moreToLoad,
      page,
      operations,
      backHandler,
      taskId
    } = this.props;

    const scrollProps = {
      data,
      componentProps: {
        onClick: id => {
          operations.redirect("/worklog/view/" + id);
        }
      },
      fetch: () => (moreToLoad && fetched ? this.fetchNext() : null),
      hasMore: moreToLoad,
      renderComponent: WorklogItem
    };

    const addButton = (
      <Button
        onClick={() => operations.redirect("/task/" + taskId + "/add-worklog")}
        variant="contained"
        color="primary"
        fullWidth
      >
        New worklog
      </Button>
    );

    return (
      <Page>
        <ScrollableContent>{renderAsyncScroll(scrollProps)}</ScrollableContent>
        {addButton}
      </Page>
    );
  }
}

const mapStateToProps = state => {
  const {
    dataSelector,
    fetchedSelector,
    moreToLoadSelector,
    totalSelector,
    pageSelector
  } = worklogListSelectors;
  return {
    data: dataSelector(state),
    moreToLoad: moreToLoadSelector(state),
    page: pageSelector(state),
    total: totalSelector(state),
    fetched: fetchedSelector(state)
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, worklogListOperations, navigationOperations),
    dispatch
  )
});

WorklogListContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  data: PropTypes.array.isRequired,
  fetched: PropTypes.bool
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorklogListContainer);
