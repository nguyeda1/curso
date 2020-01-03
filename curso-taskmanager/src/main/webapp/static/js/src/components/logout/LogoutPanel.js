import React from "react";
import { withStyles, Typography, Toolbar, Button } from "material-ui";
import PropTypes from "prop-types";

function LogoutPanel(props) {
  const { classes, logoutHandler, username } = props;
  return (
    <Toolbar>
      <Typography className={classes.header} variant="body1">
        You are logged as <strong>{username}</strong>
      </Typography>
      <Button onClick={() => logoutHandler()} className={classes.link}>
        logout
      </Button>
    </Toolbar>
  );
}

LogoutPanel.propTypes = {
  classes: PropTypes.object
};

const styles = {
  header: { flex: 1 },
  link: {
    textDecoration: "underline",
    textTransform: "initial",
    minWidth: "unset",
    padding: 0
  }
};

export default withStyles(styles)(LogoutPanel);
