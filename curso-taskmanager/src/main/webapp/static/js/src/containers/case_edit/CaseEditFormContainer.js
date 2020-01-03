import React from "react";
import PropTypes from "prop-types";
import SlideDialog from "es6!src/components/styled_components/SlideDialog";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import CaseEditHeader from "es6!src/components/case_edit/CaseEditHeader";
import CaseEditForm from "es6!src/components/case_edit/CaseEditForm";
import ListingListContainer from "es6!src/containers/listing_list/ListingListContainer";
import BookingListContainer from "es6!src/containers/booking_list/BookingListContainer";
import UserListContainer from "es6!src/containers/user_list/UserListContainer";
import InfiniteScroller from "es6!src/components/styled_components/InfiniteScroller";
import { Typography, ListItem, ListItemText } from "material-ui";
import SquareAvatar from "es6!src/components/styled_components/SquareAvatar";

class CaseEditFormContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      listing: undefined,
      booking: undefined,
      owner: undefined,
      description: "",
      selectDialog: "",
      loaded: false,
      nameError: undefined,
      listingError: undefined,
      listingDialog: false,
      bookingDialog: false,
      ownerDialog: false
    };
  }

  static getDerivedStateFromProps(props, state) {
    const { name, listing, booking, owner, description, fetched } = props;
    const { loaded } = state;
    return loaded && fetched
      ? state
      : { name, listing, booking, owner, description, loaded: true };
  }

  handleNameChange(event) {
    this.setState({ name: event.target.value, nameError: undefined });
  }

  handleDescriptionChange(event) {
    this.setState({ description: event.target.value });
  }

  handleListingSelect(value) {
    this.setState({
      listing: value,
      listingError: undefined,
      booking: undefined,
      listingDialog: false
    });
  }

  handleBookingSelect(value) {
    this.setState({ booking: value, bookingDialog: false });
  }

  handleOwnerSelect(value) {
    this.setState({ owner: value, ownerDialog: false });
  }

  handleToggleDialog(value) {
    this.setState({ selectDialog: value });
  }

  handleBookingDialogToggle() {
    const { bookingDialog } = this.state;
    this.setState({ bookingDialog: !bookingDialog });
  }

  handleListingDialogToggle() {
    const { listingDialog } = this.state;
    this.setState({ listingDialog: !listingDialog });
  }

  handleOwnerDialogToggle() {
    const { ownerDialog } = this.state;
    this.setState({ ownerDialog: !ownerDialog });
  }

  validateForm() {
    const { name, listing } = this.state;
    const nameError = name ? undefined : "Name is required";
    if (nameError) {
      this.setState({ nameError });
      return false;
    }
    return true;
  }

  render() {
    const { listingDialog, ownerDialog, bookingDialog, listing } = this.state;
    const { existing, submitHandler, backHandler } = this.props;

    const listingDialogWindow = (
      <SlideDialog
        open={listingDialog}
        onClose={this.handleListingDialogToggle}
      >
        <ListingListContainer
          backHandler={this.handleListingDialogToggle.bind(this)}
          selectHandler={this.handleListingSelect.bind(this)}
          renderAsyncScroll={props => (
            <InfiniteScroller
              loadMore={props.fetch}
              hasMore={props.hasMore}
              isReverse={props.reverse}
            >
              {props.optionalComponent ? props.optionalComponent : null}
              {props.data.map(c => (
                <props.renderComponent
                  {...props.componentProps}
                  key={c.id}
                  data={c}
                />
              ))}
            </InfiniteScroller>
          )}
        />
      </SlideDialog>
    );

    const bookingDialogWindow = (
      <SlideDialog
        open={bookingDialog}
        onClose={this.handleBookingDialogToggle}
      >
        <BookingListContainer
          listingId={listing ? listing.id : null}
          selectHandler={this.handleBookingSelect.bind(this)}
          backHandler={this.handleBookingDialogToggle.bind(this)}
          renderAsyncScroll={props => (
            <InfiniteScroller
              loadMore={props.fetch}
              hasMore={props.hasMore}
              isReverse={props.reverse}
            >
              {props.optionalComponent ? props.optionalComponent : null}
              {props.data.map(c => (
                <props.renderComponent
                  {...props.componentProps}
                  key={c.id}
                  data={c}
                />
              ))}
            </InfiniteScroller>
          )}
        />
      </SlideDialog>
    );

    const ownerDialogWindow = (
      <SlideDialog open={ownerDialog} onClose={this.handleOwnerDialogToggle}>
        <UserListContainer
          selectHandler={this.handleOwnerSelect.bind(this)}
          backHandler={this.handleOwnerDialogToggle.bind(this)}
          renderAsyncScroll={props => (
            <InfiniteScroller
              loadMore={props.fetch}
              hasMore={props.hasMore}
              isReverse={props.reverse}
            >
              {props.optionalComponent ? props.optionalComponent : null}
              {props.data.map(c => (
                <props.renderComponent
                  {...props.componentProps}
                  key={c.id}
                  data={c}
                />
              ))}
            </InfiniteScroller>
          )}
        />
      </SlideDialog>
    );

    return (
      <div>
        <Panel>
          <CaseEditHeader
            backHandler={backHandler}
            existing={existing}
            submitHandler={() => {
              if (this.validateForm()) {
                submitHandler(this.state);
              }
            }}
          />
          <ScrollableContent>
            <CaseEditForm
              {...this.state}
              existing={existing}
              nameChangeHandler={this.handleNameChange.bind(this)}
              descriptionChangeHandler={this.handleDescriptionChange.bind(this)}
              listingDialogToggleHandler={this.handleListingDialogToggle.bind(
                this
              )}
              bookingDialogToggleHandler={this.handleBookingDialogToggle.bind(
                this
              )}
              ownerDialogToggleHandler={this.handleOwnerDialogToggle.bind(this)}
            />
          </ScrollableContent>
        </Panel>
        {listingDialog ? listingDialogWindow : null}
        {bookingDialog ? bookingDialogWindow : null}
        {ownerDialog ? ownerDialogWindow : null}
      </div>
    );
  }
}

CaseEditFormContainer.propTypes = {};

export default CaseEditFormContainer;
