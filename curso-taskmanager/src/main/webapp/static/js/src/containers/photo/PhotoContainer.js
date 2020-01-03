import React from "react";
import { withStyles, Icon, Button, Dialog } from "material-ui";
import PropTypes from "prop-types";
import b64toBlob from "es6!src/helpers/b64toBlob";
import Loader from "es6!src/components/styled_components/Loader";

class PhotoContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = { opened: false };
  }

  handlePhotoDialogToggle() {
    const { opened } = this.state;
    this.setState({ opened: !opened });
  }

  render() {
    const { classes, photo, removeHandler } = this.props;
    const { opened } = this.state;
    const { name, stream, path } = photo;
    const file = b64toBlob(stream, "image/jpeg");
    const fileUrl = URL.createObjectURL(file);

    const dialog = (
      <Dialog
        fullWidth
        open={opened}
        onClose={this.handlePhotoDialogToggle.bind(this)}
      >
        <img className={classes.imgDialog} src={fileUrl} />
      </Dialog>
    );
    return (
      <div className={classes.imgContainer}>
        <Icon
          onClick={() => removeHandler(photo)}
          className={classes.imgButton}
        >
          close
        </Icon>
        <img
          onClick={() => this.handlePhotoDialogToggle()}
          className={classes.image}
          src={fileUrl}
        />
        {dialog}
      </div>
    );
  }
}

const styles = {
  imgDialog: {
    width: "100%",
    height: "auto"
  },
  imgContainer: {
    position: "relative",
    marginRight: "10px"
  },
  image: {
    height: 70,
    width: 70
  },
  imgButton: {
    top: "65%",
    left: "65%",
    position: "absolute",
    backgroundColor: "white",
    borderRadius: "100%"
  }
};

PhotoContainer.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(PhotoContainer);
