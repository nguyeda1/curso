import React from "react";
import {
  activityListOperations,
  activityListSelectors
} from "es6!src/ducks/activity_list/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import moment from "moment";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import ActivityListHeader from "es6!src/components/activity_list/ActivityListHeader";
import ActivityListItem from "es6!src/components/activity_list/ActivityListItem";
import withAsyncSearch from "es6!src/helpers/withAsyncSearch";

class ActivityListContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { operations, page, fetched } = this.props;
    const pageSize = 10;
    operations.fetchList(0, pageSize);
  }

  componentWillUnmount() {
    const { operations } = this.props;
    operations.tagAsRead();
  }

  fetchNext(e) {
    const { operations, page, fetched } = this.props;
    const pageSize = 10;
    const max = page > 0 ? pageSize * page : pageSize;
    operations.fetchNext(page, pageSize);
  }

  render() {
    const {
      renderAsyncScroll,
      data,
      fetched,
      moreToLoad,
      page,
      operations,
      lastRead
    } = this.props;

    const scrollProps = {
      data,
      fetch: () => (moreToLoad && fetched ? this.fetchNext() : null),
      hasMore: moreToLoad,
      renderComponent: ActivityListItem,
      componentProps: {
        redirect: activity => {
          operations.notificationRedirect(activity);
        },
        isNew: dateString => {
          const createdOn = moment(dateString);
          return createdOn.diff(moment(lastRead)) > 0;
        }
      }
    };

    return (
      <Panel>
        <ActivityListHeader />
        <ScrollableContent>{renderAsyncScroll(scrollProps)}</ScrollableContent>
      </Panel>
    );
  }
}

const mapStateToProps = state => {
  const {
    dataSelector,
    fetchedSelector,
    moreToLoadSelector,
    totalSelector,
    pageSelector,
    lastReadSelector
  } = activityListSelectors;
  return {
    data: dataSelector(state),
    moreToLoad: moreToLoadSelector(state),
    page: pageSelector(state),
    total: totalSelector(state),
    fetched: fetchedSelector(state),
    lastRead: lastReadSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, activityListOperations, navigationOperations),
    dispatch
  )
});

ActivityListContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  data: PropTypes.array.isRequired,
  fetched: PropTypes.bool
};

export default connect(mapStateToProps, mapDispatchToProps)(
  ActivityListContainer
);
