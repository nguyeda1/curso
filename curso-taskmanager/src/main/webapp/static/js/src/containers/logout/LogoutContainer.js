import React from "react";
import { withRouter } from "react-router-dom";
import {
  sessionOperations,
  sessionSelectors
} from "es6!src/ducks/session/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import LogoutPanel from "es6!src/components/logout/LogoutPanel";
import { getCurrentUser } from "es6!src/helpers/localStorage";

class LogoutContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  handleLogout() {
    const { operations } = this.props;
    operations.logout();
  }

  render() {
    const { username } = this.props;
    return (
      <LogoutPanel
        username={username}
        logoutHandler={this.handleLogout.bind(this)}
      />
    );
  }
}

LogoutContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  username: PropTypes.string
};

const mapStateToProps = state => {
  const { currentUsernameSelector } = sessionSelectors;
  return {
    username: currentUsernameSelector()
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, sessionOperations, navigationOperations),
    dispatch
  )
});

export default connect(mapStateToProps, mapDispatchToProps)(
  withRouter(LogoutContainer)
);
