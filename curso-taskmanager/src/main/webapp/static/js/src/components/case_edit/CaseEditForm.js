import React from "react";
import {
  CardContent,
  FormControl,
  InputLabel,
  Input,
  FormHelperText,
  ListItemText,
  Typography,
  withStyles
} from "material-ui";
import moment from "moment";
import PropTypes from "prop-types";
import ListingItem from "es6!src/components/listing_list/ListingItem";
import BookingItem from "es6!src/components/booking_list/BookingItem";
import UserItem from "es6!src/components/user_list/UserItem";
import SelectBox from "es6!src/components/styled_components/SelectBox";
import BasicItem from "es6!src/components/styled_components/BasicItem";

const CaseEditForm = props => {
  const {
    classes,
    name,
    description,
    listing,
    booking,
    owner,
    changeHandler,
    listingDialogToggleHandler,
    bookingDialogToggleHandler,
    ownerDialogToggleHandler,
    nameError,
    listingError,
    nameChangeHandler,
    descriptionChangeHandler,
    existing
  } = props;

  const nameErrorText = nameError ? (
    <FormHelperText>{nameError}</FormHelperText>
  ) : null;

  const listingErrorText = listingError ? (
    <FormHelperText>{listingError}</FormHelperText>
  ) : null;

  const ownerSelectbox = !existing ? (
    <FormControl className={classes.formControl}>
      <InputLabel shrink htmlFor="owner">
        Owner
      </InputLabel>
      <SelectBox
        value={owner}
        renderValue={owner => (
          <BasicItem avatar="U" classes={{ root: classes.selectedItem }}>
            <Typography>{owner.fullname}</Typography>
          </BasicItem>
        )}
        htmlFor="owner"
        onClick={() => {
          ownerDialogToggleHandler();
        }}
      />
    </FormControl>
  ) : null;

  return (
    <CardContent>
      <form className={classes.form}>
        <FormControl
          error={nameError ? true : false}
          onChange={event => nameChangeHandler(event)}
          required
          className={classes.formControl}
        >
          <InputLabel shrink htmlFor="name">
            Name
          </InputLabel>
          <Input value={name} id="name" />
          {nameErrorText}
        </FormControl>
        <FormControl className={classes.formControl}>
          <InputLabel shrink htmlFor="listing">
            Listing
          </InputLabel>
          <SelectBox
            value={listing}
            renderValue={listing => {
              return listing ? (
                <BasicItem avatar="L" classes={{ root: classes.selectedItem }}>
                  <Typography>{listing.name}</Typography>
                </BasicItem>
              ) : null;
            }}
            htmlFor="listing"
            onClick={() => {
              listingDialogToggleHandler();
            }}
          />
        </FormControl>
        <FormControl className={classes.formControl}>
          <InputLabel shrink htmlFor="booking">
            Booking
          </InputLabel>
          <SelectBox
            disabled={listing ? false : true}
            value={booking}
            renderValue={booking => {
              const start = moment(booking.startDate).format("DD.MM.YYYY");
              const end = moment(booking.endDate).format("DD.MM.YYYY");
              const stayInterval = start + " - " + end;
              const guest = booking.guestName;
              return (
                <BasicItem classes={{ root: classes.selectedItem }} avatar="B">
                  <ListItemText>
                    <Typography variant="body1">{stayInterval}</Typography>
                    <Typography variant="caption">{guest}</Typography>
                  </ListItemText>
                </BasicItem>
              );
            }}
            htmlFor="booking"
            onClick={() => {
              if (listing) {
                bookingDialogToggleHandler();
              }
            }}
          />
        </FormControl>
        {ownerSelectbox}
        <FormControl
          onChange={event => descriptionChangeHandler(event)}
          className={classes.formControl}
        >
          <InputLabel shrink htmlFor="description">
            Description
          </InputLabel>
          <Input multiline rowsMax={3} value={description} id="description" />
        </FormControl>
      </form>
    </CardContent>
  );
};

const styles = {
  form: { width: "100%" },
  formControl: { width: "inherit" },
  selectedItem: { padding: 0 }
};

CaseEditForm.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(CaseEditForm);
