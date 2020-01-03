import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import {
  caseDetailHistorySelectors,
  caseDetailHistoryOperations
} from "es6!src/ducks/case_detail/history/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import CaseDetailHistoryTab from "es6!src/components/case_detail/CaseDetailHistoryTab";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import CaseHistoryItem from "es6!src/components/case_detail/CaseHistoryItem";

class CaseDetailHistoryTabContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { fetched, operations, total, caseId, page } = this.props;
    const pageSize = 10;
    operations.fetchListByCaseId(caseId, 0, pageSize);
  }

  fetchNext(e) {
    const { operations, caseId, page } = this.props;
    const pageSize = 10;
    operations.fetchNext(caseId, page, pageSize);
  }

  render() {
    const {
      renderAsyncScroll,
      data,
      fetched,
      moreToLoad,
      page,
      operations
    } = this.props;

    const scrollProps = {
      data,
      fetch: () => (moreToLoad && fetched ? this.fetchNext() : null),
      hasMore: moreToLoad,
      renderComponent: CaseHistoryItem,
      componentProps: {
        redirect: activity => {
          if (activity.task) {
            operations.notificationRedirect(activity);
          }
        }
      }
    };
    return (
      <ScrollableContent>{renderAsyncScroll(scrollProps)}</ScrollableContent>
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
  } = caseDetailHistorySelectors;
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
    Object.assign({}, caseDetailHistoryOperations, navigationOperations),
    dispatch
  )
});

CaseDetailHistoryTabContainer.propTypes = {
  data: PropTypes.array.isRequired,
  operations: PropTypes.object.isRequired,
  fetched: PropTypes.bool,
  moreToLoad: PropTypes.bool,
  total: PropTypes.number
};

export default connect(mapStateToProps, mapDispatchToProps)(
  CaseDetailHistoryTabContainer
);
