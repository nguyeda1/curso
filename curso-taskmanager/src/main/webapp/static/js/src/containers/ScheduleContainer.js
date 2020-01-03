import React from "react";
import NavigationContainer from "es6!src/containers/navigation/NavigationContainer";
import Routes from "es6!src/components/routes/Routes";
import {
  activityListSelectors,
  activityListOperations
} from "es6!src/ducks/activity_list/index";
import { sessionSelectors } from "es6!src/ducks/session/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import {
  actionFulfilled,
  actionError,
  GET,
  API_URL
} from "es6!src/helpers/ajax";
import { withRouter } from "react-router-dom";
import moment from "moment";
import { getCurrentUser, isAuthenticated } from "es6!src/helpers/localStorage";

class ScheduleContainer extends React.Component {
  constructor(props, context) {
    super(props);
    this.interval = null;
  }
  componentDidMount() {
    const { operations } = this.props;
    operations.fetchNew(true);
    this.interval = setInterval(() => {
      operations.fetchNew();
    }, 30000); // every 30 seconds
  }
  componentWillUnmount() {
    clearInterval(this.interval);
  }

  render() {
    return null;
  }
}

const mapStateToProps = state => {
  const { lastReadSelector } = activityListSelectors;
  return {
    lastRead: lastReadSelector(state)
    // lastFetch: lastFetchSelector(state)
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, activityListOperations),
    dispatch
  )
});

export default connect(mapStateToProps, mapDispatchToProps)(ScheduleContainer);
