import React from "react";
import { Route, Redirect } from "react-router-dom";
import { sessionSelectors } from "es6!src/ducks/session/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { withRouter } from "react-router-dom";

// const PrivateRoute = ({
//   component: Component,
//   render: renderComponent,
//   authed,
//   ...rest
// }) => (
//   <Route
//     {...rest}
//     render={props =>
//       authed ? (
//         renderComponent ? (
//           renderComponent(props)
//         ) : (
//           <Component {...props} />
//         )
//       ) : (
//         <Redirect to="/login" />
//       )
//     }
//   />
// );
//
// export default PrivateRoute;

class PrivateRoute extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { Component, render, authed, ...rest } = this.props;

    return (
      <Route
        {...rest}
        render={props =>
          authed ? (
            render ? (
              render(props)
            ) : (
              <Component {...props} />
            )
          ) : (
            <Redirect to="/login" />
          )
        }
      />
    );
  }
}

const mapStateToProps = state => {
  const { isAuthenticatedSelector } = sessionSelectors;
  return {
    authed: isAuthenticatedSelector(state)
  };
};

export default connect(mapStateToProps)(PrivateRoute);
