import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import { feedbackEditOperations } from "es6!src/ducks/feedback_edit/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import FeedbackEditHeader from "es6!src/components/feedback_edit/FeedbackEditHeader";
import FeedbackEditForm from "es6!src/components/feedback_edit/FeedbackEditForm";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";

class FeedbackEditContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      hostInformed: true,
      guestInformed: false,
      type: "EMAIL",
      description: ""
    };
  }

  handleDescriptionChange(event) {
    this.setState({ description: event.target.value });
  }

  handleSentToChange() {
    this.setState({
      hostInformed: !this.state.hostInformed,
      guestInformed: !this.state.guestInformed
    });
  }

  handleTypeChange(value) {
    this.setState({ type: value });
  }

  handleSubmit() {
    const { operations, match } = this.props;
    const caseId = match.params.id;
    operations.editFeedback(caseId, this.state);
  }

  handleBack() {
    const { history } = this.props;
    history.goBack();
  }

  render() {
    const { operations } = this.props;
    return (
      <Panel>
        <FeedbackEditHeader
          backHandler={this.handleBack.bind(this)}
          submitHandler={this.handleSubmit.bind(this)}
        />
        <ScrollableContent>
          <FeedbackEditForm
            {...this.state}
            typeChangeHandler={this.handleTypeChange.bind(this)}
            sentToHandler={this.handleSentToChange.bind(this)}
            descriptionChangeHandler={this.handleDescriptionChange.bind(this)}
          />
        </ScrollableContent>
      </Panel>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  return {};
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, feedbackEditOperations),
    dispatch
  )
});

FeedbackEditContainer.propTypes = {
  operations: PropTypes.object.isRequired
};

export default connect(mapStateToProps, mapDispatchToProps)(
  FeedbackEditContainer
);
