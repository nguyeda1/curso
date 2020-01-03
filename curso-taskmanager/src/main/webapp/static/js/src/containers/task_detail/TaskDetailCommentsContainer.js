import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import {
  taskDetailCommentsSelectors,
  taskDetailCommentsOperations
} from "es6!src/ducks/task_detail/comments/index";
import { Button, InputAdornment, FormControl } from "material-ui";
import { bindActionCreators } from "redux";
import CommentItem from "es6!src/components/comment/CommentItem";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import Panel from "es6!src/components/styled_components/Panel";
import StyledInput from "es6!src/components/styled_components/StyledInput";
import FixAboveNav from "es6!src/components/styled_components/FixAboveNav";
import { getCurrentUser } from "es6!src/helpers/localStorage";
import withComments from "es6!src/helpers/withComments";

class TaskDetailCommentsContainer extends React.Component {
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

TaskDetailCommentsContainer.propTypes = {};

const mapStateToProps = state => {
  return {};
};

export default withComments({
  getId: taskDetailCommentsSelectors.taskIdSelector,
  getData: taskDetailCommentsSelectors.sortByDateSelector,
  getPage: taskDetailCommentsSelectors.pageSelector,
  getHasMore: taskDetailCommentsSelectors.moreToLoadSelector,
  Item: CommentItem,
  fetch: taskDetailCommentsOperations.fetchListByTaskId,
  fetchNext: taskDetailCommentsOperations.fetchNext,
  addComment: taskDetailCommentsOperations.addItem
})(connect(mapStateToProps)(TaskDetailCommentsContainer));
