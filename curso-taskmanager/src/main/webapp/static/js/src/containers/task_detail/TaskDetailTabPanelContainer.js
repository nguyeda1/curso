import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import {
  taskDetailTabPanelOperations,
  taskDetailTabPanelSelectors
} from "es6!src/ducks/task_detail/tab_panel/index";
import { Tabs } from "material-ui";
import NormalizedTab from "es6!src/components/styled_components/NormalizedTab";
import RightBadge from "es6!src/components/styled_components/RightBadge";
import CaseDetailTabPanel from "es6!src/components/case_detail/CaseDetailTabPanel";

class TaskDetailTabPanelContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  handleTabSelect(event, selectedTab) {
    const { switchTab } = this.props;
    switchTab(selectedTab);
  }

  render() {
    const { commentCount, checklistCount, selectedTab } = this.props;

    return (
      <Tabs
        value={selectedTab}
        onChange={this.handleTabSelect.bind(this)}
        indicatorColor="primary"
        textColor="primary"
        scrollable
        scrollButtons="off"
        fullWidth
      >
        <NormalizedTab key="General" value="General" label="General" />
        <NormalizedTab
          key="Checklist"
          value="Checklist"
          label={
            <RightBadge badgeContent={checklistCount}>Checklist</RightBadge>
          }
        />
        <NormalizedTab
          key="Comments"
          value="Comments"
          label={<RightBadge badgeContent={commentCount}>Comments</RightBadge>}
        />
      </Tabs>
    );
  }
}

const mapStateToProps = state => {
  const {
    checklistSizeSelector,
    commentSizeSelector
  } = taskDetailTabPanelSelectors;
  return {
    commentCount: commentSizeSelector(state),
    checklistCount: checklistSizeSelector(state)
  };
};

TaskDetailTabPanelContainer.propTypes = {
  selectedTab: PropTypes.string.isRequired
};

export default connect(mapStateToProps)(TaskDetailTabPanelContainer);
