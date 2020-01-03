import React from "react";
import {
  TextField,
  Typography,
  Button,
  withStyles,
  Switch,
  FormControlLabel
} from "material-ui";
import PropTypes from "prop-types";
import LinkButton from "es6!src/components/styled_components/LinkButton";

const LoginForm = props => {
  const {
    classes,
    children,
    toggleHandler,
    changeUsernameHandler,
    changePasswordHandler,
    submitHandler,
    passwordError,
    usernameError,
    username,
    password,
    savePass,
    savePassHandler
  } = props;

  return (
    <div className={classes.content}>
      <Typography color="primary" variant="headline" gutterBottom>
        Login
      </Typography>
      <form className={classes.form} onSubmit={submitHandler}>
        <TextField
          onChange={event => changeUsernameHandler(event)}
          value={username}
          id="username"
          label="Username"
          margin="normal"
          error={usernameError ? true : false}
          helperText={usernameError}
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          value={password}
          onChange={event => changePasswordHandler(event)}
          id="password"
          type="password"
          label="Password"
          margin="normal"
          error={passwordError ? true : false}
          helperText={passwordError}
          InputLabelProps={{ shrink: true }}
        />

        <Button
          type="submit"
          className={classes.button}
          color="primary"
          variant="contained"
        >
          Login
        </Button>
        <div className={classes.bottomDiv}>
          <FormControlLabel
            control={
              <Switch
                checked={savePass}
                onChange={savePassHandler}
                value="savePass"
                color="primary"
              />
            }
            label="Remember me"
          />
          <LinkButton onClick={() => toggleHandler()}>
            Forgotten password?
          </LinkButton>
        </div>
      </form>
    </div>
  );
};

const styles = theme => ({
  content: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    height: "100vh",
    justifyContent: "center"
  },
  form: {
    maxWidth: "200px",
    display: "flex",
    flexDirection: "column"
  },
  button: {
    marginTop: 8
  },
  link: {
    textDecoration: "underline",
    textTransform: "initial"
  },

  bottomDiv: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center"
  }
});

LoginForm.propTypes = {
  classes: PropTypes.object,
  toggleHandler: PropTypes.func
};

export default withStyles(styles)(LoginForm);