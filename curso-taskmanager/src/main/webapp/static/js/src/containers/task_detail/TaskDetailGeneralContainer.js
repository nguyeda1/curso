import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { getCurrentUser } from "es6!src/helpers/localStorage";
import {
  taskDetailGeneralSelectors,
  taskDetailGeneralOperations
} from "es6!src/ducks/task_detail/general/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import { bindActionCreators } from "redux";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import TaskDetailGeneral from "es6!src/components/task_detail/TaskDetailGeneral";
import withPhotoPanel from "es6!src/helpers/withPhotoPanel";
import HideableDescriptionContainer from "es6!src/containers/hideable_description/HideableDescriptionContainer";

class TaskDetailGeneralContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { operations, taskId } = this.props;
    operations.fetchById(taskId);
  }

  handleListingRedirect(listing) {
    const { operations } = this.props;
    operations.internalAppRedirect(
      `${getCurrentUser().baseUrl}/app/listing/view.xhtml?id=${listing.id}`
    );
  }

  handleCaseRedirect(c) {
    const { operations } = this.props;
    operations.redirect(`/case/view/${c.id}`);
  }

  render() {
    const { data, fetched, taskId, photoPanel } = this.props;
    const description = (
      <HideableDescriptionContainer description={data.description} />
    );
    return (
      <ScrollableContent>
        <TaskDetailGeneral
          renderPhotoPanel={fetched ? photoPanel : null}
          data={data}
          renderDescription={description}
          listingRedirect={this.handleListingRedirect.bind(this)}
          caseRedirect={this.handleCaseRedirect.bind(this)}
        />
      </ScrollableContent>
    );
  }
}

TaskDetailGeneralContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  data: PropTypes.object.isRequired,
  fetched: PropTypes.bool
};

const mapStateToProps = state => {
  const { taskSelector, fetchedSelector } = taskDetailGeneralSelectors;
  return {
    data: taskSelector(state),
    fetched: fetchedSelector(state)
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, taskDetailGeneralOperations, navigationOperations),
    dispatch
  )
});

export default withPhotoPanel({
  getId: taskDetailGeneralSelectors.taskIdSelector,
  fetch: taskDetailGeneralOperations.fetchPhotos,
  upload: taskDetailGeneralOperations.uploadPhoto,
  remove: taskDetailGeneralOperations.deletePhoto,
  getPhotos: taskDetailGeneralSelectors.photosSelector,
  isLoading: taskDetailGeneralSelectors.photosLoadingSelector
})(connect(mapStateToProps, mapDispatchToProps)(TaskDetailGeneralContainer));
