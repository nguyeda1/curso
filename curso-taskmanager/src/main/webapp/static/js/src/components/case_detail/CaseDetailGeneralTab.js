import React from "react";
import {
  CardContent,
  Typography,
  Button,
  Divider,
  CardActions,
  withStyles,
  InputLabel,
  FormControl,
  Badge,
  Icon
} from "material-ui";
import PropTypes from "prop-types";
import moment from "moment";
import ListingItem from "es6!src/components/listing_list/ListingItem";
import BookingItem from "es6!src/components/booking_list/BookingItem";
import UserItem from "es6!src/components/user_list/UserItem";
import TagPanel from "es6!src/components/styled_components/TagPanel";
import PhotoPanel from "es6!src/components/styled_components/PhotoPanel";
import BasicItem from "es6!src/components/styled_components/BasicItem";
import StateTag from "es6!src/components/styled_components/StateTag";
import Priority from "es6!src/components/styled_components/Priority";
import LinkButton from "es6!src/components/styled_components/LinkButton";
import BasicListingItem from "es6!src/components/styled_components/BasicListingItem";

const CaseDetailGeneralTab = props => {
  const {
    data,
    tags,
    classes,
    renderTagDialog,
    tagDialogHandler,
    renderPhotoPanel,
    renderDescription,
    iAmFollower,
    iAmAssociate,
    followHandler,
    listingRedirect
  } = props;

  const deadline = data.deadline ? (
    <Typography variant="caption">
      Deadline: {moment(data.deadline).format("DD.MMMM, HH:mm")}
    </Typography>
  ) : null;
  const finishedOn = data.finishedOn ? (
    <Typography variant="caption">
      Finished: {moment(data.finishedOn).format("DD.MMMM, HH:mm")}
    </Typography>
  ) : null;
  const listing = data.listing ? (
    <FormControl>
      <InputLabel shrink>Listing</InputLabel>
      <BasicListingItem
        onClick={() => listingRedirect(data.listing)}
        data={data.listing}
      />
    </FormControl>
  ) : null;

  const start = data.booking
    ? moment(data.booking.startDate).format("DD.MM.YYYY")
    : null;
  const end = data.booking
    ? moment(data.booking.endDate).format("DD.MM.YYYY")
    : null;
  const stayInterval = start && end ? start + " - " + end : null;

  const booking = data.booking ? (
    <FormControl>
      <InputLabel shrink>Booking</InputLabel>
      <BasicItem avatar="B">
        <Typography variant="body1">{stayInterval}</Typography>
        <Typography variant="caption">{data.booking.guestName}</Typography>
        <Typography className={classes.canceled} variant="caption">
          {data.booking.canceled ? "CANCELED" : ""}
        </Typography>
      </BasicItem>
    </FormControl>
  ) : null;
  const owner = data.owner ? (
    <FormControl>
      <InputLabel shrink>Owner</InputLabel>
      <BasicItem avatar="U">
        <Typography>{data.owner.fullname}</Typography>
      </BasicItem>
    </FormControl>
  ) : null;
  const assignee = data.assignee ? (
    <FormControl>
      <InputLabel shrink>Assignee</InputLabel>
      <BasicItem avatar="U">
        <Typography>{data.assignee.fullname}</Typography>
      </BasicItem>
    </FormControl>
  ) : null;
  const tagPanel = (
    <FormControl>
      <InputLabel shrink>Tags</InputLabel>
      <TagPanel tags={tags} showTagListHandler={() => tagDialogHandler()} />
    </FormControl>
  );

  const photoPanel = (
    <FormControl>
      <InputLabel shrink>Photos</InputLabel>
      {renderPhotoPanel}
    </FormControl>
  );

  const state = data.caseState ? <StateTag>{data.caseState}</StateTag> : null;
  const priority = data.priority ? (
    <Priority showText>{data.priority}</Priority>
  ) : null;
  const followButtonText = iAmFollower ? "Unfollow" : "Follow";

  const followButton = !iAmAssociate ? (
    <Button size="small" color="primary" onClick={() => followHandler()}>
      {followButtonText}
    </Button>
  ) : null;

  return (
    <div>
      <CardContent className={classes.basic}>
        <div className={classes.mainInfo}>
          <div className={classes.header}>
            <Typography variant="subheading">
              <strong>{data.name}</strong>
            </Typography>
            {followButton}
          </div>
          <div className={classes.states}>
            {state}
            {priority}
          </div>
        </div>
        {deadline}
        {finishedOn}
      </CardContent>
      {renderDescription}
      <Divider />
      <CardContent className={classes.additional}>
        {listing}
        {booking}
        {owner}
        {assignee}
        {renderTagDialog}
        {photoPanel}
      </CardContent>
    </div>
  );
};

const styles = theme => ({
  mainInfo: {
    display: "flex",
    flexDirection: "column"
  },
  header: { display: "inline-flex", alignItems: "baseline" },
  states: { display: "inline-flex" },
  canceled: { color: "red" },
  basic: {
    flexDirection: "column",
    alignItems: "flex-start"
  },
  wordBreak: { wordWrap: "break-word" },
  formControl: { width: "inherit" },
  additional: {
    display: "flex",
    flexDirection: "column"
  }
});

CaseDetailGeneralTab.propTypes = {
  classes: PropTypes.object,
  data: PropTypes.object
};

export default withStyles(styles)(CaseDetailGeneralTab);
