import React from "react";
import { withStyles, Icon, Button } from "material-ui";
import PropTypes from "prop-types";
import b64toBlob from "es6!src/helpers/b64toBlob";
import Loader from "es6!src/components/styled_components/Loader";

const PhotoPanel = props => {
  const { classes, children, uploadHandler, loading } = props;

  const loader = loading ? (
    <div>
      <Loader />
    </div>
  ) : null;

  return (
    <div className={classes.panel}>
      {children}
      {loader}
      <input
        accept="image/*"
        className={classes.hiddenInput}
        id="raised-button-file"
        multiple
        type="file"
        onChange={uploadHandler}
      />
      <label htmlFor="raised-button-file">
        <Button mini variant="fab" color="primary" component="span">
          <Icon>cloud_upload</Icon>
        </Button>
      </label>
    </div>
  );
};

const styles = {
  panel: {
    marginTop: "20px",
    display: "flex",
    flexWrap: "wrap",
    alignItems: "center"
  },
  hiddenInput: { display: "none" }
};

PhotoPanel.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(PhotoPanel);
