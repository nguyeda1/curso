import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";

import PropTypes from "prop-types";
import {
  caseDetailFeedbackSelectors,
  caseDetailFeedbackOperations
} from "es6!src/ducks/case_detail/feedback/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";

import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import Panel from "es6!src/components/styled_components/Panel";
import CaseDetailFeedbackItem from "es6!src/components/case_detail/CaseDetailFeedbackItem";
import { Button } from "material-ui";
import SlideDialog from "es6!src/components/styled_components/SlideDialog";
import FeedbackEditContainer from "es6!src/containers/feedback_edit/FeedbackEditContainer";
import FixAboveNav from "es6!src/components/styled_components/FixAboveNav";

class CaseDetailFeedbackContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = { feedbackDialog: false };
  }

  componentDidMount() {
    const { operations, page, caseId, fetched } = this.props;
    const pageSize = 10;
    const max = page > 0 ? pageSize * page : pageSize;
    operations.fetchList(caseId, 0, pageSize);
  }

  fetchNext(e) {
    const { operations, page, fetched, caseId } = this.props;
    const pageSize = 10;
    const max = page > 0 ? pageSize * page : pageSize;
    operations.fetchNext(caseId, page, pageSize);
  }

  handleCheck(id) {
    const { operations } = this.props;
    operations.finishTask(id);
  }

  handleAssign(id) {
    const { operations } = this.props;
    operations.assign(id);
  }

  render() {
    const {
      renderAsyncScroll,
      data,
      fetched,
      moreToLoad,
      page,
      operations,
      caseId,
      isReview
    } = this.props;
    const { feedbackDialog } = this.state;

    const scrollProps = {
      data,
      fetch: () => (moreToLoad && fetched ? this.fetchNext() : null),
      hasMore: moreToLoad,
      renderComponent: CaseDetailFeedbackItem
    };

    const addFeedbackButton = isReview ? (
      <Button
        onClick={() => operations.redirect("/case/add-feedback/" + caseId)}
        variant="contained"
        color="primary"
        fullWidth
      >
        Add feedback
      </Button>
    ) : null;

    return (
      <Panel>
        <Panel>
          <ScrollableContent>
            {renderAsyncScroll(scrollProps)}
          </ScrollableContent>
        </Panel>
        {addFeedbackButton}
      </Panel>
    );
  }
}

const mapStateToProps = state => {
  const {
    dataSelector,
    fetchedSelector,
    moreToLoadSelector,
    totalSelector,
    pageSelector,
    isReviewSelector
  } = caseDetailFeedbackSelectors;
  return {
    data: dataSelector(state),
    moreToLoad: moreToLoadSelector(state),
    page: pageSelector(state),
    total: totalSelector(state),
    fetched: fetchedSelector(state),
    isReview: isReviewSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, caseDetailFeedbackOperations, navigationOperations),
    dispatch
  )
});

CaseDetailFeedbackContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  data: PropTypes.array.isRequired,
  fetched: PropTypes.bool,
  moreToLoad: PropTypes.bool,
  total: PropTypes.number
};

export default connect(mapStateToProps, mapDispatchToProps)(
  CaseDetailFeedbackContainer
);
