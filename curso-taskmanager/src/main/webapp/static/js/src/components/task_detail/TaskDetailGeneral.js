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
  Avatar,
  ListItemText,
  Badge,
  Icon
} from "material-ui";
import PropTypes from "prop-types";
import moment from "moment";
import CaseListItem from "es6!src/components/case_list/CaseListItem";
import BookingItem from "es6!src/components/booking_list/BookingItem";
import UserItem from "es6!src/components/user_list/UserItem";
import TagPanel from "es6!src/components/styled_components/TagPanel";
import PhotoPanel from "es6!src/components/styled_components/PhotoPanel";
import BasicItem from "es6!src/components/styled_components/BasicItem";
import Priority from "es6!src/components/styled_components/Priority";
import normalizeText from "es6!src/helpers/normalizeText";
import StateTag from "es6!src/components/styled_components/StateTag";
import BasicListingItem from "es6!src/components/styled_components/BasicListingItem";
import BasicCaseItem from "es6!src/components/styled_components/BasicCaseItem";

const TaskDetailGeneral = props => {
  const {
    data,
    classes,
    renderPhotoPanel,
    renderDescription,
    listingRedirect,
    caseRedirect
  } = props;

  const finished = data.finished ? "(Finished)" : "";

  const cursoCase = data.cursoCase ? data.cursoCase : null;

  const locality = data.locality ? (
    <Typography variant="caption">Locality: {data.locality}</Typography>
  ) : null;

  const cursoCaseComp = cursoCase ? (
    <FormControl>
      <InputLabel shrink>Case</InputLabel>
      <BasicCaseItem data={cursoCase} onClick={() => caseRedirect(cursoCase)} />
    </FormControl>
  ) : null;

  const listingComp = data.listing ? (
    <FormControl>
      <InputLabel shrink>Listing</InputLabel>
      <BasicListingItem
        onClick={() => listingRedirect(data.listing)}
        data={data.listing}
      />
    </FormControl>
  ) : null;

  const photoPanel = (
    <FormControl>
      <InputLabel shrink>Photos</InputLabel>
      {renderPhotoPanel}
    </FormControl>
  );

  const assignee =
    data.assignee && data.assignee.id ? (
      <FormControl>
        <InputLabel shrink>Assignee</InputLabel>
        <BasicItem avatar="U">
          <Typography>{data.assignee.fullname}</Typography>
        </BasicItem>
      </FormControl>
    ) : null;
  const deadline = data.deadline ? (
    <Typography variant="caption">
      Deadline: {moment(data.deadline).format("DD.MMMM, HH:mm")}
    </Typography>
  ) : null;
  const plannedOn = data.plannedOn ? (
    <Typography variant="caption">
      Planned on: {moment(data.plannedOn).format("DD.MMMM, HH:mm")}
    </Typography>
  ) : null;

  const finishedOn = data.finishedOn ? (
    <Typography variant="caption">
      Finished on: {moment(data.finishedOn).format("DD.MMMM, HH:mm")}
    </Typography>
  ) : null;

  const finishedState = data.finishedState ? (
    <Typography variant="caption">
      Finished state: {normalizeText(data.finishedState)}
    </Typography>
  ) : null;
  const priority = data.priority ? (
    <Priority showText>{data.priority}</Priority>
  ) : null;

  const state = data.finishedState ? (
    <StateTag>{data.finishedState}</StateTag>
  ) : !data.finished ? (
    <StateTag>{"IN_PROGRESS"}</StateTag>
  ) : null;

  const priorityOnScreen = data.priorityOnScreen ? (
    <Typography>
      <strong>&nbsp;({data.priorityOnScreen})</strong>
    </Typography>
  ) : (
    ""
  );
  const problem = data.problemText ? (
    <Typography variant="caption">Problem: {data.problemText}</Typography>
  ) : null;

  return (
    <div>
      <CardContent className={classes.basic}>
        <Typography variant="subheading">
          <strong>{data.name}</strong>
        </Typography>
        <div className={classes.states}>
          {state} {priority} {priorityOnScreen}
        </div>
        {locality}
        {deadline}
        {plannedOn}
        {finishedOn}
        {finishedState}
        {problem}
      </CardContent>
      {renderDescription}
      <Divider />
      <CardContent className={classes.additional}>
        {cursoCaseComp}
        {listingComp}
        {assignee}
        {photoPanel}
      </CardContent>
    </div>
  );
};

const styles = theme => ({
  basic: {
    flexDirection: "column",
    alignItems: "flex-start"
  },
  states: { display: "inline-flex", alignItems: "flex-end" },
  wordBreak: { wordWrap: "break-word" },
  formControl: { width: "inherit" },
  additional: {
    display: "flex",
    flexDirection: "column"
  }
});

TaskDetailGeneral.propTypes = {
  classes: PropTypes.object,
  data: PropTypes.object
};

export default withStyles(styles)(TaskDetailGeneral);
