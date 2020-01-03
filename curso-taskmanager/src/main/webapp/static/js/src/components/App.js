import React from "react";
import NavigationContainer from "es6!src/containers/navigation/NavigationContainer";
import Routes from "es6!src/components/routes/Routes";

import {
  sessionSelectors,
  sessionOperations
} from "es6!src/ducks/session/index";

import { errorSelectors } from "es6!src/ducks/error/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";

import { isAuthenticated } from "es6!src/helpers/localStorage";
import ScheduleContainer from "es6!src/containers/ScheduleContainer";
import { GET, API_URL, whoami } from "es6!src/helpers/ajax";
import { withStyles } from "material-ui";
import { withRouter } from "react-router-dom";
import Content from "es6!src/components/styled_components/Content";
import ErrorContainer from "es6!src/containers/error/ErrorContainer";

class App extends React.Component {
  constructor(props, context) {
    super(props);
    const { operations } = this.props;
    operations.whoami();
  }

  componentDidMount() {
    const { operations } = this.props;

    var elem = document.getElementById("main_loader");
    elem.style.display = "none";
    // whoami()
    //   .then(response => {
    //     const user = response.data;
    //     if (user) {
    //       setCurrentUser(user);
    //     } else {
    //       unsetCurrentUser();
    //     }
    //   })
    //   .catch(error => {
    //     console.log(error);
    //   });
  }

  render() {
    const { classes, isAuthenticated } = this.props;
    const navigation = isAuthenticated ? <NavigationContainer /> : null;
    const scheduler = isAuthenticated ? <ScheduleContainer /> : null;

    return (
      <div className={classes.app}>
        <Content>
          <Routes />
        </Content>
        {scheduler}
        {navigation}
        <ErrorContainer />
      </div>
    );
  }
}
const styles = {
  app: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    overflow: "hidden"
  }
};

const mapStateToProps = state => {
  const { isAuthenticatedSelector } = sessionSelectors;

  return {
    isAuthenticated: isAuthenticatedSelector(state)
    // lastFetch: lastFetchSelector(state)
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(Object.assign({}, sessionOperations), dispatch)
});

export default withStyles(styles)(
  withRouter(
    connect(
      mapStateToProps,
      mapDispatchToProps
    )(App)
  )
);
