import React from "react";
import { getCurrentUser } from "es6!src/helpers/localStorage";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import StyledInput from "es6!src/components/styled_components/StyledInput";
import { Button, InputAdornment, FormControl } from "material-ui";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import Panel from "es6!src/components/styled_components/Panel";

const withComments = ({
  getId,
  getData,
  getPage,
  getHasMore,
  Item,
  addComment,
  fetch,
  fetchNext
}) => Component => {
  class WithComments extends React.Component {
    constructor(props) {
      super(props);
      this.state = { comment: "" };
      this.scrollHeight = 0;
    }

    componentDidMount() {
      const { actions, id, page } = this.props;
      const pageSize = 10;

      actions.fetch(id, 0, pageSize);
    }

    componentDidUpdate() {
      if (this.scrollHeight > 0) {
        this.goToPrevScroll(this.scrollHeight);
      } else {
        const dummy = document.getElementById("dummy");
        dummy.scrollIntoView();
      }
    }

    goToPrevScroll(oldScrollHeight) {
      const list = document.getElementById("comment-list");
      list.scrollTop = list.scrollHeight - oldScrollHeight + list.scrollTop;
    }

    handleCommentChange(event) {
      this.setState({ [event.target.id]: event.target.value });
    }
    handleSubmitComment() {
      const { actions, id } = this.props;
      const { comment } = this.state;
      actions.addComment(id, { text: comment }, () => {
        this.setState({ comment: "" });
      });
    }

    commentIsMine(item) {
      return item.assignee.id === getCurrentUser().id;
    }

    fetchNext(e) {
      const { actions, id, page } = this.props;
      const pageSize = 10;
      const list = document.getElementById("comment-list");
      this.scrollHeight = list.scrollHeight;
      actions.fetchNext(id, page, pageSize);
    }

    render() {
      const { hasMore, data } = this.props;
      const asyncList = (
        <InfiniteScroller
          loadMore={this.fetchNext.bind(this)}
          hasMore={hasMore}
          isReverse={true}
          threshold={1}
        >
          {data.map(c => (
            <Item
              commentIsMine={this.commentIsMine.bind(this)}
              key={c.id}
              data={c}
            />
          ))}
        </InfiniteScroller>
      );

      const addCommentInput = (
        <StyledInput
          id="comment"
          placeholder="Type new comment"
          value={this.state.comment}
          onChange={this.handleCommentChange.bind(this)}
          endAdornment={
            <InputAdornment position="end">
              <Button
                onClick={this.handleSubmitComment.bind(this)}
                size="small"
                color="primary"
              >
                Send
              </Button>
            </InputAdornment>
          }
        />
      );

      const content = (
        <ScrollableContent id="comment-list">
          {asyncList}
          <div id="dummy" />
        </ScrollableContent>
      );
      return (
        <Component
          {...this.props}
          renderComments={content}
          renderCommentInput={addCommentInput}
        />
      );
    }
  }

  const mapStateToProps = () => {
    return (state, ownProps) => {
      const id = getId(state, ownProps);
      const page = getPage(state, ownProps);
      const hasMore = getHasMore(state, ownProps);
      const data = getData(state, ownProps);
      return { id, data, page, hasMore };
    };
  };

  const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators({ fetch, fetchNext, addComment }, dispatch)
  });

  return connect(mapStateToProps, mapDispatchToProps)(WithComments);

  WithComments.propTypes = {};
};
export default withComments;
