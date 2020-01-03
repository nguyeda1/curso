import React from "react";
import {
  withStyles,
  Typography,
  CardContent,
  CardActions,
  Button,
  InputLabel,
  FormControl
} from "material-ui";
import PropTypes from "prop-types";

const HideableDecription = props => {
  const {
    classes,
    children,
    className,
    showMoreHandler,
    more,
    showButton
  } = props;
  const button = showButton ? (
    <CardActions className={classes.basic}>
      <Button onClick={() => showMoreHandler()} size="small" color="primary">
        Show {more ? "less" : "more"}
      </Button>
    </CardActions>
  ) : null;
  return (
    <div>
      <CardContent className={classes.basic}>
        <Typography
          id="description_content"
          className={classes.wordBreak}
          noWrap={!more}
          variant="body1"
        >
          {children}
        </Typography>
      </CardContent>
      {button}
    </div>
  );
};

const styles = {
  basic: {
    flexDirection: "column",
    alignItems: "flex-start"
  }
  //wordBreak: { wordWrap: "break-word" }
};

HideableDecription.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(HideableDecription);
