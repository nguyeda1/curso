import React from "react";
import {
  CardContent,
  FormControl,
  InputLabel,
  Input,
  FormHelperText,
  Toolbar,
  withStyles,
  TextField,
  Typography,
  FormControlLabel,
  Checkbox
} from "material-ui";
import PropTypes from "prop-types";
import ListingItem from "es6!src/components/listing_list/ListingItem";
import BookingItem from "es6!src/components/booking_list/BookingItem";
import UserItem from "es6!src/components/user_list/UserItem";
import SelectChip from "es6!src/components/styled_components/SelectChip";
import SelectChipPanel from "es6!src/components/styled_components/SelectChipPanel";
import SelectBox from "es6!src/components/styled_components/SelectBox";
import BasicItem from "es6!src/components/styled_components/BasicItem";

const WorklogEditForm = props => {
  const {
    classes,
    isProjectHandler,
    valueChangeHandler,
    timeChangeHandler,
    dialogToggleHandler,
    invoiceToggleHandler,
    values,
    errors
  } = props;
  const {
    from,
    to,
    category,
    kmCount,
    vehicle,
    type,
    description,
    categories,
    isProject,
    toInvoice
  } = values;

  const {
    categoryError,
    fromError,
    toError,
    kmError,
    vehicleError,
    typeError
  } = errors;
  let kmInput,
    vehicleInput,
    invoiceInput,
    typeInput = null;
  if (category) {
    const { invoicable, transport } = category;
    kmInput = transport ? (
      <FormControl
        required
        onChange={valueChangeHandler}
        className={classes.formControl}
      >
        <InputLabel shrink htmlFor="kmCount">
          KM
        </InputLabel>
        <Input value={kmCount} id="kmCount" />
        {kmError ? <FormHelperText error>{kmError}</FormHelperText> : null}
      </FormControl>
    ) : null;

    vehicleInput = transport ? (
      <FormControl required id="vehicle" className={classes.formControl}>
        <InputLabel shrink htmlFor="vehicle">
          Vehicle
        </InputLabel>
        <SelectBox
          value={vehicle}
          renderValue={vehicle => {
            return vehicle ? (
              <BasicItem classes={{ root: classes.selectedItem }}>
                <Typography>{vehicle}</Typography>
              </BasicItem>
            ) : null;
          }}
          htmlFor="vehicle"
          onClick={() => {
            dialogToggleHandler("vehicle");
          }}
        />
        {vehicleError ? (
          <FormHelperText error>{vehicleError}</FormHelperText>
        ) : null}
      </FormControl>
    ) : null;

    invoiceInput = invoicable ? (
      <FormControlLabel
        control={
          <Checkbox
            id="toInvoice"
            checked={toInvoice}
            onChange={invoiceToggleHandler}
            color="primary"
          />
        }
        label="To invoice"
      />
    ) : null;

    typeInput = invoicable ? (
      <FormControl required id="type" className={classes.formControl}>
        <InputLabel shrink htmlFor="type">
          Work type
        </InputLabel>
        <SelectBox
          value={type}
          renderValue={type => {
            return type ? (
              <BasicItem classes={{ root: classes.selectedItem }}>
                <Typography>{type}</Typography>
              </BasicItem>
            ) : null;
          }}
          htmlFor="type"
          onClick={() => {
            dialogToggleHandler("type");
          }}
        />
        {typeError ? <FormHelperText error>{typeError}</FormHelperText> : null}
      </FormControl>
    ) : null;
  }

  return (
    <CardContent>
      <form className={classes.form}>
        <FormControl required className={classes.formControl}>
          <InputLabel shrink>Type</InputLabel>
          <SelectChipPanel>
            <SelectChip
              label="Category"
              onClick={() => isProjectHandler()}
              clicked={!isProject}
            />
            <SelectChip
              label="Project"
              onClick={() => isProjectHandler()}
              clicked={isProject}
            />
          </SelectChipPanel>
        </FormControl>
        <FormControl required className={classes.formControl}>
          <InputLabel shrink htmlFor="wCategory">
            Worklog category
          </InputLabel>
          <SelectBox
            value={category}
            renderValue={category => {
              return category ? (
                <BasicItem classes={{ root: classes.selectedItem }}>
                  <Typography>{category.name}</Typography>
                </BasicItem>
              ) : null;
            }}
            htmlFor="wCategory"
            onClick={() => {
              dialogToggleHandler("category");
            }}
          />
          {categoryError ? (
            <FormHelperText error>{categoryError}</FormHelperText>
          ) : null}
        </FormControl>
        <TextField
          id="from"
          required
          onChange={event => timeChangeHandler(event)}
          label="Start"
          type="time"
          value={from}
          helperText={
            fromError ? (
              <FormHelperText error>{fromError}</FormHelperText>
            ) : null
          }
          className={classes.formControl}
          InputLabelProps={{
            shrink: true
          }}
          inputProps={{
            step: 300 // 5 min
          }}
        />
        <TextField
          id="to"
          required
          helperText={
            toError ? <FormHelperText error>{toError}</FormHelperText> : null
          }
          value={to}
          onChange={event => timeChangeHandler(event)}
          label="End"
          type="time"
          className={classes.formControl}
          InputLabelProps={{
            shrink: true
          }}
          inputProps={{
            step: 300 // 5 min
          }}
        />
        {kmInput}
        {vehicleInput}
        {invoiceInput}
        {typeInput}
        <FormControl
          onChange={valueChangeHandler}
          className={classes.formControl}
        >
          <InputLabel shrink htmlFor="description">
            Description
          </InputLabel>
          <Input value={description} multiline rowsMax={3} id="description" />
        </FormControl>
      </form>
    </CardContent>
  );
};

const styles = theme => ({
  form: { width: "100%" },
  formControl: {
    width: "inherit"
  },
  selectedItem: { padding: 0 }
});

WorklogEditForm.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(WorklogEditForm);
