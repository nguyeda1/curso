import React from "react";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import { caseListOperations } from "es6!src/ducks/case_list/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import Panel from "es6!src/components/styled_components/Panel";
import CaseListHeader from "es6!src/components/case_list/CaseListHeader";
import CaseListFilter from "es6!src/components/case_list/CaseListFilter";
import CaseListContentContainer from "es6!src/containers/case_list/CaseListContentContainer";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";

import Loader from "es6!src/components/styled_components/Loader";
class CaseListContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = { filterValue: "MY_CASES" };
  }

  handleTabSelect(event, state) {
    const { operations } = this.props;
    const pageSize = 10;
    operations.fetchCases(state, 0, pageSize);
    this.setState({ filterValue: state });
  }

  handleCreateCase() {
    const { operations } = this.props;
    const path = `/case/new`;
    operations.redirect(path);
  }

  render() {
    const { filterValue } = this.state;
    const tabs = ["MY_CASES", "ALL", "NEW", "IN_PROGRESS", "REVIEW", "DONE"];
    return (
      <Panel>
        <CaseListHeader createCaseHandler={this.handleCreateCase.bind(this)} />
        <CaseListFilter
          selectHandler={this.handleTabSelect.bind(this)}
          value={filterValue}
          tabs={tabs}
        />
        <CaseListContentContainer
          filter={filterValue}
          renderAsyncScroll={props => (
            <InfiniteScroller
              loadMore={props.fetch}
              hasMore={props.hasMore}
              isReverse={props.reverse}
            >
              {props.data.map(c => (
                <props.renderComponent
                  {...props.componentProps}
                  key={c.id}
                  data={c}
                />
              ))}
            </InfiniteScroller>
          )}
        />
      </Panel>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, navigationOperations, caseListOperations),
    dispatch
  )
});

CaseListContainer.propTypes = {
  operations: PropTypes.object.isRequired
};

export default connect(null, mapDispatchToProps)(CaseListContainer);
