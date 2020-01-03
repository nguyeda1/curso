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
  Icon,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody
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
import HideableDescriptionContainer from "es6!src/containers/hideable_description/HideableDescriptionContainer";

const WorklogDetailTab = props => {
  const { data, classes } = props;
  const { tableRows, from, to, category, description, user, date, task } = data;

  const dateOutput = date ? moment(data.date).format("DD.MM.YYYY") : null;

  const interval =
    moment(from).format("HH:mm") + " - " + moment(to).format("HH:mm");

  return (
    <div>
      <CardContent className={classes.basic}>
        <div className={classes.mainInfo}>
          <div className={classes.header}>
            <Typography variant="subheading">
              <strong>{data.category.name}</strong>
            </Typography>
          </div>
        </div>
        <Typography variant="caption">
          <strong>{dateOutput}</strong>
        </Typography>
        <Typography variant="caption">
          <strong>{interval}</strong>
        </Typography>
      </CardContent>
      <HideableDescriptionContainer description={description} />
      <Divider />
      <CardContent className={classes.additional}>
        <FormControl>
          <InputLabel shrink>Created by</InputLabel>
          <BasicItem avatar="U">
            <Typography>
              {user.firstname} {user.lastname}
            </Typography>
          </BasicItem>
        </FormControl>
        <FormControl>
          <InputLabel shrink>Task</InputLabel>
          <BasicItem avatar="T">
            <Typography>{task.name}</Typography>
          </BasicItem>
        </FormControl>
      </CardContent>
      <Divider />
      <CardContent>
        <Table className={classes.table}>
          <TableBody>
            {tableRows.map((row, id) => (
              <TableRow key={`row_${id}`}>
                <TableCell
                  key={`head_${id}`}
                  variant="head"
                  padding="none"
                  component="th"
                  scope="row"
                >
                  {row.name}
                </TableCell>
                <TableCell key={`cell_${id}`} variant="body" align="right">
                  {row.value}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
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

WorklogDetailTab.propTypes = {
  classes: PropTypes.object,
  data: PropTypes.object
};

export default withStyles(styles)(WorklogDetailTab);
