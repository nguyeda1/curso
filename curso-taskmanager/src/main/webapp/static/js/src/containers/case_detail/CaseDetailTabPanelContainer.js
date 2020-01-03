import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import {
  caseDetailTabPanelOperations,
  caseDetailTabPanelSelectors
} from "es6!src/ducks/case_detail/tab_panel/index";

import CaseDetailTabPanel from "es6!src/components/case_detail/CaseDetailTabPanel";

class CaseDetailTabPanelContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  handleTabSelect(event, selectedTab) {
    const { operations } = this.props;
    operations.switchTab(selectedTab);
  }

  render() {
    const { tasksAmount, selectedTab, tabChangeHandler } = this.props;

    return (
      <CaseDetailTabPanel
        tasksAmount={tasksAmount}
        selected={selectedTab}
        selectHandler={tabChangeHandler}
      />
    );
  }
}

const mapStateToProps = state => {
  const {
    taskListSizeSelector,
    selectedSelector
  } = caseDetailTabPanelSelectors;
  return {
    tasksAmount: taskListSizeSelector(state)
    //  selectedTab: selectedSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, caseDetailTabPanelOperations),
    dispatch
  )
});

CaseDetailTabPanelContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  tasksAmount: PropTypes.number.isRequired,
  selectedTab: PropTypes.string.isRequired
};

export default connect(mapStateToProps, mapDispatchToProps)(
  CaseDetailTabPanelContainer
);
