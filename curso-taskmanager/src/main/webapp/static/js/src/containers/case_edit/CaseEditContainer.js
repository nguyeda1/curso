import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import {
  caseEditSelectors,
  caseEditOperations
} from "es6!src/ducks/case_edit/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";

import CaseEditFormContainer from "es6!src/containers/case_edit/CaseEditFormContainer";

class CaseEditContainer extends React.Component {
  constructor(props) {
    super(props);
    const { history, operations, match, existing } = this.props;
    this.init();
    this.handleRedirectBetweenNewEdit();
  }

  init() {
    const { operations, match, existing, fetched } = this.props;
    const id = match.params.id;
    if (!fetched) {
      operations.clearCaseEdit();
      if (existing && id) {
        operations.fetchCaseEdit(id);
      } else {
        operations.fetchCleanEditForm();
      }
    }
  }

  handleRedirectBetweenNewEdit() {
    const { history, operations, match, existing } = this.props;
    history.listen((location, action) => {
      var path = location.pathname;
      if (path === "/case/new") {
        operations.clearCaseEdit();
        operations.fetchCleanEditForm();
      }
      if (path.startsWith("/case/edit/")) {
        operations.clearCaseEdit();
        const id = path.split("/")[3];
        operations.fetchCaseEdit(id);
      }
    });
  }

  handleSubmit(state) {
    const { operations, match } = this.props;
    const { name, listing, booking, owner, description } = state;
    var id = match.params.id;
    const data = { id, name, listing, booking, owner, description };
    operations.editCase(data, response => {
      const path = `/case/view/${response.id}`;
      operations.redirect(path);
    });
  }

  handleBack() {
    const { history } = this.props;
    const path = `/case/list`;
    history.goBack();
  }

  render() {
    const { operations, match, existing, fetched } = this.props;
    const id = match.params.id;
    const { name, listing, booking, owner, description } = this.props;
    const form = fetched ? (
      <CaseEditFormContainer
        id={id}
        {...this.props}
        submitHandler={this.handleSubmit.bind(this)}
        backHandler={this.handleBack.bind(this)}
        existing={existing}
      />
    ) : null;
    return form;
  }
}

const mapStateToProps = (state, ownProps) => {
  const {
    nameSelector,
    listingSelector,
    bookingSelector,
    ownerSelector,
    descriptionSelector,
    fetchedSelector
  } = caseEditSelectors;
  return {
    name: nameSelector(state),
    listing: listingSelector(state),
    booking: bookingSelector(state),
    owner: ownerSelector(state),
    description: descriptionSelector(state),
    fetched: fetchedSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, caseEditOperations, navigationOperations),
    dispatch
  )
});

CaseEditContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  name: PropTypes.string,
  description: PropTypes.string,
  owner: PropTypes.object,
  booking: PropTypes.object,
  listing: PropTypes.object,
  existing: PropTypes.bool,
  fetched: PropTypes.bool
};

export default connect(mapStateToProps, mapDispatchToProps)(CaseEditContainer);
