import React from "react";
import { navigationOperations } from "es6!src/ducks/navigation/index";

import {
  caseListSelectors,
  caseListOperations
} from "es6!src/ducks/case_list/index";

import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import Panel from "es6!src/components/styled_components/Panel";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import CaseListItem from "es6!src/components/case_list/CaseListItem";
import CaseListTotal from "es6!src/components/case_list/CaseListTotal";
import withAsyncSearch from "es6!src/helpers/withAsyncSearch";

class CaseListContentContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { operations, fetched, total, page, filter } = this.props;
    const pageSize = 10;
    const max = page * pageSize;
    operations.fetchCases(filter, 0, pageSize);
  }

  handleShowDetail(value) {
    const { operations, match } = this.props;
    const path = `/case/view/${value}`;
    operations.redirect(path);
  }

  fetchNext(e) {
    const { operations, page, filter } = this.props;
    const pageSize = 10;
    operations.fetchNext(filter, page, pageSize);
  }

  render() {
    const {
      renderAsyncScroll,
      data,
      fetched,
      moreToLoad,
      page,
      operations,
      total,
      searchBar
    } = this.props;
    const scrollProps = {
      data,
      fetch: () => (moreToLoad && fetched ? this.fetchNext() : null),
      hasMore: moreToLoad,
      renderComponent: CaseListItem,
      componentProps: { onClick: this.handleShowDetail.bind(this) }
    };

    return (
      <Panel>
        {searchBar}
        <CaseListTotal data={total} />
        <ScrollableContent>{renderAsyncScroll(scrollProps)}</ScrollableContent>
      </Panel>
    );
  }
}

const mapStateToProps = () => {
  const {
    dataSelector,
    fetchedSelector,
    moreToLoadSelector,
    totalSelector,
    pageSelector
  } = caseListSelectors;
  return (state, ownProps) => {
    return {
      data: dataSelector(state, ownProps),
      moreToLoad: moreToLoadSelector(state, ownProps),
      page: pageSelector(state, ownProps),
      total: totalSelector(state, ownProps),
      fetched: fetchedSelector(state, ownProps)
    };
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, caseListOperations, navigationOperations),
    dispatch
  )
});

CaseListContentContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  cases: PropTypes.array,
  total: PropTypes.number,
  fetched: PropTypes.bool
};

export default withAsyncSearch({
  elementId: "caseQuery",
  placeholder: "Start typing name...",
  ownParams: [
    (state, ownProps) => caseListSelectors.filterValueSelector(state, ownProps)
  ],
  search: (filter, query) => caseListOperations.asyncSearch(filter, query),
  defaultFetch: filter => caseListOperations.fetchCases(filter, 0, 10),
  searchById: (filter, id) => caseListOperations.searchById(filter, id)
})(connect(mapStateToProps, mapDispatchToProps)(CaseListContentContainer));
