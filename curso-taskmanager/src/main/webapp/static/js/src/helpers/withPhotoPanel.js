import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import PhotoPanel from "es6!src/components/styled_components/PhotoPanel";
import PhotoContainer from "es6!src/containers/photo/PhotoContainer";

const withPhotoPanel = ({
  getId,
  fetch,
  upload,
  remove,
  getPhotos,
  isLoading
}) => Component => {
  class WithPhotoPanel extends React.Component {
    constructor(props) {
      super(props);
    }
    componentDidMount() {
      const { actions, entityId } = this.props;
      actions.fetch(entityId);
    }

    handleUpload(event) {
      event.preventDefault();
      const { actions, entityId } = this.props;
      const files = event.target.files;
      actions.upload(entityId, Array.from(files));
      event.target.value = null;
    }

    handleRemovePhoto(photo) {
      const { actions } = this.props;
      actions.remove(photo);
    }

    render() {
      const { photos, loading } = this.props;
      const photoPanel = (
        <PhotoPanel
          loading={loading}
          photos={photos}
          uploadHandler={this.handleUpload.bind(this)}
        >
          {photos.map((photo, index) => (
            <PhotoContainer
              key={index}
              removeHandler={this.handleRemovePhoto.bind(this)}
              photo={photo}
            />
          ))}
        </PhotoPanel>
      );
      return <Component {...this.props} photoPanel={photoPanel} />;
    }
  }

  const mapStateToProps = () => {
    return (state, ownProps) => {
      return {
        entityId: getId(state, ownProps),
        photos: getPhotos(state),
        loading: isLoading(state)
      };
    };
  };

  const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(
      {
        fetch,
        upload,
        remove
      },
      dispatch
    )
  });

  return connect(mapStateToProps, mapDispatchToProps)(WithPhotoPanel);

  WithPhotoPanel.propTypes = {};
};
export default withPhotoPanel;
