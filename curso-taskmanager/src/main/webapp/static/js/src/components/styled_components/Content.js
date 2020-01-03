import React from "react";
import { withStyles } from "material-ui";
import PropTypes from "prop-types";

const Content = props => {
  const { children, classes } = props;
  return <div className={classes.root}>{children}</div>;
};

const styles = (theme, baseHeight =90) => ({
  root: {
    padding: theme.spacing.unit,
    height: `${baseHeight}vh`,
    ["@media (max-height:600px)"]: {
      height: `${baseHeight-5}vh`
    },
    ["@media (max-height:420px)"]: {
      height: `${baseHeight-10}vh`
    },
    ["@media (max-height:320px)"]: {
      height: `${baseHeight-15}vh`
    },
    ["@media (max-height:250px)"]: {
      height: `${baseHeight-20}vh`
    },
    ["@media (max-height:200px)"]: {
      height: `${baseHeight-25}vh`
    }
  }
});

Content.propTypes = {
  classes: PropTypes.object,
  children: PropTypes.node
};

export default withStyles(styles)(Content);
