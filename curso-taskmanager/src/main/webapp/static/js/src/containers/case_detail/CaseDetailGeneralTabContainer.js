import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { getCurrentUser } from "es6!src/helpers/localStorage";
import { contains, equals, find, propEq } from "ramda";
import {
  caseDetailGeneralSelectors,
  caseDetailGeneralOperations
} from "es6!src/ducks/case_detail/general/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import CaseDetailGeneralTab from "es6!src/components/case_detail/CaseDetailGeneralTab";
import { bindActionCreators } from "redux";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import SlideDialog from "es6!src/components/styled_components/SlideDialog";
import TagListContainer from "es6!src/containers/tag_list/TagListContainer";
import withPhotoPanel from "es6!src/helpers/withPhotoPanel";
import HideableDescriptionContainer from "es6!src/containers/hideable_description/HideableDescriptionContainer";

class CaseDetailGeneralTabContainer extends React.Component {
  constructor(props) {
    super(props);
    const { operations, caseId } = props;
    this.state = { tagDialog: false };
  }

  componentDidMount() {
    const { operations, caseId } = this.props;
    operations.fetchById(caseId);
  }

  handleTagDialogToggle() {
    const { tagDialog } = this.state;
    this.setState({ tagDialog: !tagDialog });
  }

  handleFollow() {
    const { operations, caseId } = this.props;
    operations.followCase(caseId);
  }

  handleListingRedirect(listing) {
    const { operations } = this.props;
    operations.internalAppRedirect(
      `${getCurrentUser().baseUrl}/app/listing/view.xhtml?id=${listing.id}`
    );
  }

  render() {
    const { data, tags, fetched, caseId, photoPanel } = this.props;
    const { more, tagDialog } = this.state;
    const tagDialogWindow = tagDialog ? (
      <SlideDialog open={tagDialog} onClose={this.handleTagDialogToggle}>
        <TagListContainer
          caseId={caseId}
          backHandler={this.handleTagDialogToggle.bind(this)}
        />
      </SlideDialog>
    ) : null;
    const description = (
      <HideableDescriptionContainer description={data.description} />
    );
    const currentUser = getCurrentUser();
    const general = fetched ? (
      <CaseDetailGeneralTab
        {...this.state}
        data={data}
        tags={tags}
        tagDialogHandler={this.handleTagDialogToggle.bind(this)}
        renderTagDialog={tagDialogWindow}
        renderPhotoPanel={fetched ? photoPanel : null}
        renderDescription={description}
        followHandler={this.handleFollow.bind(this)}
        iAmFollower={find(propEq("id", currentUser.id))(data.followers)}
        iAmAssociate={
          equals(currentUser.id)(data.owner ? data.owner.id : null) ||
          equals(currentUser.id)(data.assignee ? data.assignee.id : null) ||
          equals(currentUser.id)(data.createdBy ? data.createdBy.id : null)
        }
        listingRedirect={this.handleListingRedirect.bind(this)}
      />
    ) : null;

    return <ScrollableContent>{general}</ScrollableContent>;
  }
}

CaseDetailGeneralTabContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  data: PropTypes.object.isRequired,
  fetched: PropTypes.bool
};

const mapStateToProps = state => {
  const {
    caseSelector,
    caseTagsSelector,
    fetchedSelector
  } = caseDetailGeneralSelectors;
  return {
    data: caseSelector(state),
    tags: caseTagsSelector(state),
    fetched: fetchedSelector(state)
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, caseDetailGeneralOperations, navigationOperations),
    dispatch
  )
});

export default withPhotoPanel({
  getId: caseDetailGeneralSelectors.caseIdSelector,
  fetch: caseDetailGeneralOperations.fetchPhotos,
  upload: caseDetailGeneralOperations.uploadPhoto,
  remove: caseDetailGeneralOperations.deletePhoto,
  getPhotos: caseDetailGeneralSelectors.photosSelector,
  isLoading: caseDetailGeneralSelectors.photosLoadingSelector
})(connect(mapStateToProps, mapDispatchToProps)(CaseDetailGeneralTabContainer));
