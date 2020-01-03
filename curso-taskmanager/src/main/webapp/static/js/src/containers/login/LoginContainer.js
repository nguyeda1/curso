import React from "react";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import LoginForm from "es6!src/components/login_page/LoginForm";
import ForgotPasswordDialog from "es6!src/components/login_page/ForgotPasswordDialog";
import LoginFailDialog from "es6!src/components/login_page/LoginFailDialog";
import { getCurrentUser, getPassword } from "es6!src/helpers/localStorage";
import {
  sessionOperations,
  sessionSelectors
} from "es6!src/ducks/session/index";
import { activityListOperations } from "es6!src/ducks/activity_list/index";
import moment from "moment";

class LoginContainer extends React.Component {
  constructor(props) {
    super(props);
    const {
      operations,
      error,
      isAuthenticated,
      isPasswordSaved,
      savedPassword,
      currentUser
    } = props;
    if (isAuthenticated) {
      operations.redirect("/case/list");
    }
    this.state = {
      forgotPasswordDialog: false,
      loginFailDialog: error ? true : false,
      username: getCurrentUser() ? getCurrentUser().username : "",
      password: getPassword() ? getPassword() : "",
      usernameError: undefined,
      passwordError: undefined,
      isPasswordSaved
    };
  }

  handleUsernameChange(event) {
    this.setState({ username: event.target.value, usernameError: undefined });
  }

  handlePasswordChange(event) {
    this.setState({ password: event.target.value, passwordError: undefined });
  }

  handleSubmit(event) {
    event.preventDefault();
    if (this.validateForm()) {
      const { operations, history } = this.props;
      const { username, password, isPasswordSaved } = this.state;
      operations.login(
        username,
        password,
        isPasswordSaved,
        this.handleToggleLoginFail.bind(this)
      );
    }
  }

  validateForm() {
    const { username, password } = this.state;
    const usernameError = username ? undefined : "Username is required";
    const passwordError = password ? undefined : "Password is required";
    if (usernameError || passwordError) {
      this.setState({ usernameError, passwordError });
      return false;
    }
    return true;
  }

  handleSavePass() {
    const { isPasswordSaved, operations } = this.state;
    this.setState({ isPasswordSaved: !isPasswordSaved });
  }

  handleToggleForgotPassword() {
    this.setState({ forgotPasswordDialog: !this.state.forgotPasswordDialog });
  }

  handleToggleLoginFail() {
    this.setState({ loginFailDialog: !this.state.loginFailDialog });
  }

  render() {
    const {
      forgotPasswordDialog,
      loginFailDialog,
      usernameError,
      passwordError,
      username,
      isPasswordSaved,
      password
    } = this.state;

    const {
      savedPassword,
      isLoading,
      isAuthenticated,
      operations
    } = this.props;

    if (isAuthenticated) {
      operations.redirect("/case/list");
    }

    return (
      <div>
        <LoginForm
          username={username}
          password={password}
          changeUsernameHandler={this.handleUsernameChange.bind(this)}
          changePasswordHandler={this.handlePasswordChange.bind(this)}
          toggleHandler={this.handleToggleForgotPassword.bind(this)}
          submitHandler={this.handleSubmit.bind(this)}
          usernameError={usernameError}
          passwordError={passwordError}
          savePass={isPasswordSaved}
          savePassHandler={this.handleSavePass.bind(this)}
          isLoading={isLoading}
        />

        <ForgotPasswordDialog
          toggleHandler={this.handleToggleForgotPassword.bind(this)}
          opened={forgotPasswordDialog}
        />
        <LoginFailDialog
          toggleHandler={this.handleToggleLoginFail.bind(this)}
          opened={loginFailDialog}
        />
      </div>
    );
  }
}

const mapStateToProps = state => {
  const {
    isAuthenticatedSelector,
    savedPasswordSelector,
    isPasswordSavedSelector,
    currentUserSelector,
    isLoadingSelector
  } = sessionSelectors;
  return {
    isAuthenticated: isAuthenticatedSelector(state),
    isPasswordSaved: isPasswordSavedSelector(state),
    savedPassword: savedPasswordSelector(state),
    currentUser: currentUserSelector(state),
    isLoading: isLoadingSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign(
      {},
      sessionOperations,
      navigationOperations,
      activityListOperations
    ),
    dispatch
  )
});

LoginContainer.propTypes = {
  operations: PropTypes.object.isRequired
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LoginContainer);
