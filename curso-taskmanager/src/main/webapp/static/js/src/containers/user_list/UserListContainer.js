import React from "react";
import PropTypes from "prop-types";
import {
  userListSelectors,
  userListOperations
} from "es6!src/ducks/user_list/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import UserItem from "es6!src/components/user_list/UserItem";
import UserListHeader from "es6!src/components/user_list/UserListHeader";
import { CardContent } from "material-ui";
import withAsyncSearch from "es6!src/helpers/withAsyncSearch";
import { Typography, ListItem, ListItemText } from "material-ui";
import SquareAvatar from "es6!src/components/styled_components/SquareAvatar";

class UserListContainer extends React.Component {
  constructor(props) {
    super(props);
    const { operations } = this.props;
  }

  componentDidMount() {
    const { operations, page, fetched } = this.props;
    const pageSize = 10;
    operations.fetchUsers(0, pageSize);
  }

  fetchNext(e) {
    const { operations, page, fetched } = this.props;
    const pageSize = 10;
    const max = page > 0 ? pageSize * page : pageSize;
    operations.fetchNext(page, pageSize);
  }

  render() {
    const {
      searchBar,
      renderAsyncScroll,
      data,
      fetched,
      moreToLoad,
      page,
      operations,
      selectHandler,
      backHandler
    } = this.props;

    const scrollProps = {
      data,
      optionalComponent: (
        <ListItem onClick={() => selectHandler(null)}>
          <SquareAvatar>X</SquareAvatar>
          <ListItemText>
            <Typography variant="body1">No user</Typography>
          </ListItemText>
        </ListItem>
      ),
      fetch: () => (moreToLoad && fetched ? this.fetchNext() : null),
      hasMore: moreToLoad,
      renderComponent: UserItem,
      componentProps: { onClick: selectHandler }
    };

    return (
      <Panel>
        <UserListHeader backHandler={backHandler} />
        {searchBar}
        <ScrollableContent>{renderAsyncScroll(scrollProps)}</ScrollableContent>
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
    pageSelector
  } = userListSelectors;
  return {
    data: dataSelector(state),
    moreToLoad: moreToLoadSelector(state),
    page: pageSelector(state),
    total: totalSelector(state),
    fetched: fetchedSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(Object.assign({}, userListOperations), dispatch)
});

UserListContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  data: PropTypes.array.isRequired,
  fetched: PropTypes.bool
};

export default withAsyncSearch({
  elementId: "userQuery",
  placeholder: "Start typing name or username",
  search: query => userListOperations.asyncSearch(query),
  defaultFetch: () => userListOperations.fetchUsers(0, 10),
  searchById: id => userListOperations.searchById(id)
})(connect(mapStateToProps, mapDispatchToProps)(UserListContainer));
