import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import { caseDetailTabPanelSelectors } from "es6!src/ducks/case_detail/tab_panel/index";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import CaseDetailHeader from "es6!src/components/case_detail/CaseDetailHeader";
import CaseDetailTabPanelContainer from "es6!src/containers/case_detail/CaseDetailTabPanelContainer";
import CaseDetailGeneralTabContainer from "es6!src/containers/case_detail/CaseDetailGeneralTabContainer";
import CaseDetailTaskTabContainer from "es6!src/containers/case_detail/CaseDetailTaskTabContainer";
import CaseDetailHistoryTabContainer from "es6!src/containers/case_detail/CaseDetailHistoryTabContainer";
import CaseDetailFeedbackContainer from "es6!src/containers/case_detail/CaseDetailFeedbackContainer";
import CaseDetailCommentsContainer from "es6!src/containers/case_detail/CaseDetailCommentsContainer";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";

class CaseDetailContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = { selectedTab: "Case" };
  }

  handleTabChange(event, value) {
    this.setState({ selectedTab: value });
  }

  handleBack() {
    const { history, operations } = this.props;
    history.goBack();
  }

  handleEdit() {
    const { operations, match, cursoCase } = this.props;
    const path = `/case/edit/${match.params.id}`;
    operations.redirect(path);
  }

  render() {
    const { fetched, match } = this.props;
    const { selectedTab } = this.state;
    const id = match.params.id;

    return (
      <Panel>
        <CaseDetailHeader
          editHandler={this.handleEdit.bind(this)}
          backHandler={this.handleBack.bind(this)}
        />
        <CaseDetailTabPanelContainer
          tabChangeHandler={this.handleTabChange.bind(this)}
          selectedTab={selectedTab}
        />
        {selectedTab === "Case" && (
          <CaseDetailGeneralTabContainer caseId={id} />
        )}
        {selectedTab === "Tasks" && <CaseDetailTaskTabContainer caseId={id} />}
        {selectedTab === "History" && (
          <CaseDetailHistoryTabContainer
            caseId={id}
            renderAsyncScroll={props => {
              const {
                fetch,
                hasMore,
                data,
                reverse,
                renderComponent,
                componentProps
              } = props;
              return (
                <InfiniteScroller
                  loadMore={fetch}
                  hasMore={hasMore}
                  isReverse={reverse}
                >
                  {data.map(c => (
                    <props.renderComponent
                      {...componentProps}
                      key={c.id}
                      data={c}
                    />
                  ))}
                </InfiniteScroller>
              );
            }}
          />
        )}
        {selectedTab === "Feedback" && (
          <CaseDetailFeedbackContainer
            caseId={id}
            renderAsyncScroll={props => {
              const {
                fetch,
                hasMore,
                data,
                reverse,
                renderComponent,
                componentProps
              } = props;
              return (
                <InfiniteScroller
                  loadMore={fetch}
                  hasMore={hasMore}
                  isReverse={reverse}
                >
                  {data.map(c => (
                    <props.renderComponent
                      {...componentProps}
                      key={c.id}
                      data={c}
                    />
                  ))}
                </InfiniteScroller>
              );
            }}
          />
        )}
        {selectedTab === "Comments" && (
          <CaseDetailCommentsContainer caseId={id} />
        )}
      </Panel>
    );
  }
}

const mapStateToProps = state => {
  const { selectedSelector } = caseDetailTabPanelSelectors;
  return {
    selectedTab: selectedSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(Object.assign({}, navigationOperations), dispatch)
});

CaseDetailContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  fetched: PropTypes.bool
};

export default connect(mapStateToProps, mapDispatchToProps)(
  CaseDetailContainer
);
