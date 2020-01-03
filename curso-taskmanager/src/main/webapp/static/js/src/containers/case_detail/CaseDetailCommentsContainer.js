import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import {
  caseDetailCommentsSelectors,
  caseDetailCommentsOperations
} from "es6!src/ducks/case_detail/comments/index";
import { Button, InputAdornment, FormControl } from "material-ui";
import { bindActionCreators } from "redux";
import CommentItem from "es6!src/components/comment/CommentItem";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import Panel from "es6!src/components/styled_components/Panel";
import StyledInput from "es6!src/components/styled_components/StyledInput";
import FixAboveNav from "es6!src/components/styled_components/FixAboveNav";
import { getCurrentUser } from "es6!src/helpers/localStorage";
import withComments from "es6!src/helpers/withComments";

class CaseDetailCommentsContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { renderComments, renderCommentInput } = this.props;
    return (
      <Panel>
        <Panel>{renderComments}</Panel>
        {renderCommentInput}
      </Panel>
    );
  }
}

CaseDetailCommentsContainer.propTypes = {};

const mapStateToProps = state => {
  return {};
};

export default withComments({
  getId: caseDetailCommentsSelectors.caseIdSelector,
  getData: caseDetailCommentsSelectors.sortByDateSelector,
  getPage: caseDetailCommentsSelectors.pageSelector,
  getHasMore: caseDetailCommentsSelectors.moreToLoadSelector,
  Item: CommentItem,
  fetch: caseDetailCommentsOperations.fetchListByCaseId,
  fetchNext: caseDetailCommentsOperations.fetchNext,
  addComment: caseDetailCommentsOperations.addItem
})(connect(mapStateToProps)(CaseDetailCommentsContainer));
