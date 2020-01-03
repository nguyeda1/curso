import React from "react";
import PropTypes from "prop-types";
import {
  listingListSelectors,
  listingListOperations
} from "es6!src/ducks/listing_list/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import ListingItem from "es6!src/components/listing_list/ListingItem";
import ListingListHeader from "es6!src/components/listing_list/ListingListHeader";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import { CardContent } from "material-ui";
import withAsyncSearch from "es6!src/helpers/withAsyncSearch";
import { Typography, ListItem, ListItemText } from "material-ui";
import SquareAvatar from "es6!src/components/styled_components/SquareAvatar";

class ListingListContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { operations, page, fetched } = this.props;
    const pageSize = 10;
    operations.fetchListings(0, pageSize);
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
      optionalComponent: (
        <ListItem onClick={() => selectHandler(null)}>
          <SquareAvatar>X</SquareAvatar>
          <ListItemText>
            <Typography variant="body1">No listing</Typography>
          </ListItemText>
        </ListItem>
      ),
      data,
      fetch: () => (moreToLoad && fetched ? this.fetchNext() : null),
      hasMore: moreToLoad,
      renderComponent: ListingItem,
      componentProps: { onClick: selectHandler }
    };

    return (
      <Panel>
        <ListingListHeader backHandler={backHandler} />
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
  } = listingListSelectors;
  return {
    data: dataSelector(state),
    moreToLoad: moreToLoadSelector(state),
    page: pageSelector(state),
    total: totalSelector(state),
    fetched: fetchedSelector(state)
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, listingListOperations),
    dispatch
  )
});

ListingListContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  data: PropTypes.array.isRequired,
  fetched: PropTypes.bool
};

export default withAsyncSearch({
  elementId: "listingQuery",
  placeholder: "Start typing name or address",
  search: query => listingListOperations.asyncSearch(query),
  defaultFetch: () => listingListOperations.fetchListings(0, 10),
  searchById: id => listingListOperations.searchById(id)
})(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(ListingListContainer)
);
