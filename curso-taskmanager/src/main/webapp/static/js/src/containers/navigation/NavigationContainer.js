import React from "react";
import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import Navigation from "es6!src/components/Navigation";
import {
  navigationOperations,
  navigationSelectors
} from "es6!src/ducks/navigation/index";

class NavigationContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  handleRedirect(path) {
    const { operations } = this.props;
    operations.redirect(path);
  }

  render() {
    const { currentPath, notificationCount } = this.props;
    var value =
      currentPath && currentPath.lastIndexOf("/") > 0
        ? currentPath.substring(0, currentPath.lastIndexOf("/"))
        : currentPath;
    return (
      <Navigation
        path={value}
        redirectHandler={this.handleRedirect.bind(this)}
        notificationCount={notificationCount}
      />
    );
  }
}

const mapStateToProps = state => {
  const {
    currentPathSelector,
    notificationCountSelector
  } = navigationSelectors;
  return {
    currentPath: currentPathSelector(state),
    notificationCount: notificationCountSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(navigationOperations, dispatch)
});

NavigationContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  history: PropTypes.object.isRequired,
  currentPath: PropTypes.string
};

export default connect(mapStateToProps, mapDispatchToProps)(
  withRouter(NavigationContainer)
);
