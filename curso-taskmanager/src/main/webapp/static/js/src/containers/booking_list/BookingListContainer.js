import React from "react";
import PropTypes from "prop-types";
import {
  bookingListOperations,
  bookingListSelectors
} from "es6!src/ducks/booking_list/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import BookingItem from "es6!src/components/booking_list/BookingItem";
import BookingListHeader from "es6!src/components/booking_list/BookingListHeader";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import { CardContent } from "material-ui";
import withAsyncSearch from "es6!src/helpers/withAsyncSearch";
import { Typography, ListItem, ListItemText } from "material-ui";
import SquareAvatar from "es6!src/components/styled_components/SquareAvatar";

class BookingListContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { operations, page, fetched, listingId } = this.props;
    const pageSize = 10;
    operations.fetchListByListingId(listingId, 0, pageSize);
  }

  fetchNext(e) {
    const { operations, page, fetched, listingId } = this.props;
    const pageSize = 10;
    const max = page > 0 ? pageSize * page : pageSize;
    operations.fetchNext(listingId, page, pageSize);
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
            <Typography variant="body1">No booking</Typography>
          </ListItemText>
        </ListItem>
      ),
      fetch: () => (moreToLoad && fetched ? this.fetchNext() : null),
      hasMore: moreToLoad,
      renderComponent: BookingItem,
      componentProps: { onClick: selectHandler }
    };

    return (
      <Panel>
        <BookingListHeader backHandler={backHandler} />
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
  } = bookingListSelectors;
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
    Object.assign({}, bookingListOperations),
    dispatch
  )
});

BookingListContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  data: PropTypes.array.isRequired,
  fetched: PropTypes.bool
};

export default withAsyncSearch({
  elementId: "bookingQuery",
  placeholder: "Start typing guest name",
  ownParams: [
    (state, ownProps) => bookingListSelectors.listingIdSelector(state, ownProps)
  ],
  search: (listingId, query) =>
    bookingListOperations.asyncSearch(listingId, query),
  defaultFetch: listingId =>
    bookingListOperations.fetchListByListingId(listingId, 0, 10),
  searchById: (listingId, id) => bookingListOperations.searchById(listingId, id)
})(connect(mapStateToProps, mapDispatchToProps)(BookingListContainer));
