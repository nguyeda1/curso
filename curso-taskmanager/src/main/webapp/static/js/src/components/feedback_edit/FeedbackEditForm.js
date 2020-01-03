import React from "react";
import {
  CardContent,
  FormControl,
  InputLabel,
  Input,
  FormHelperText,
  Toolbar,
  withStyles
} from "material-ui";
import PropTypes from "prop-types";
import ListingItem from "es6!src/components/listing_list/ListingItem";
import BookingItem from "es6!src/components/booking_list/BookingItem";
import UserItem from "es6!src/components/user_list/UserItem";
import SelectChip from "es6!src/components/styled_components/SelectChip";
import SelectChipPanel from "es6!src/components/styled_components/SelectChipPanel";

const FeedbackEditForm = props => {
  const {
    classes,
    sentToHandler,
    descriptionChangeHandler,
    hostInformed,
    guestInformed,
    typeChangeHandler,
    type
  } = props;

  return (
    <CardContent>
      <form className={classes.form}>
        <FormControl className={classes.formControl}>
          <InputLabel shrink>Sent to</InputLabel>
          <SelectChipPanel>
            <SelectChip
              label="Host"
              onClick={() => sentToHandler()}
              clicked={hostInformed}
            />
            <SelectChip
              label="Guest"
              onClick={() => sentToHandler()}
              clicked={guestInformed}
            />
          </SelectChipPanel>
        </FormControl>
        <FormControl className={classes.formControl}>
          <InputLabel shrink>Sent by</InputLabel>
          <SelectChipPanel>
            <SelectChip
              label="E-mail"
              clicked={type === "EMAIL"}
              value="EMAIL"
              onClick={() => typeChangeHandler("EMAIL")}
            />
            <SelectChip
              label="Phone"
              clicked={type === "PHONE"}
              value="PHONE"
              onClick={() => typeChangeHandler("PHONE")}
            />
            <SelectChip
              label="Other"
              clicked={type === "OTHER"}
              value="OTHER"
              onClick={() => typeChangeHandler("OTHER")}
            />
          </SelectChipPanel>
        </FormControl>
        <FormControl
          onChange={event => descriptionChangeHandler(event)}
          className={classes.formControl}
        >
          <InputLabel shrink htmlFor="description">
            Description
          </InputLabel>
          <Input multiline rowsMax={3} id="description" />
        </FormControl>
      </form>
    </CardContent>
  );
};

const styles = {
  form: { width: "100%" },
  formControl: { width: "inherit" }
};

FeedbackEditForm.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(FeedbackEditForm);
