import React from "react";
import {
  ListItem,
  ListItemText,
  Typography,
  Divider,
  withStyles
} from "material-ui";
import moment from "moment";
import PropTypes from "prop-types";
import ListingItem from "es6!src/components/listing_list/ListingItem";
import Item from "es6!src/components/styled_components/Item";
import normalizeText from "es6!src/helpers/normalizeText";
import StateTag from "es6!src/components/styled_components/StateTag";
import Priority from "es6!src/components/styled_components/Priority";

const CaseListItem = props => {
  const { classes, data, onClick } = props;
  const name = data.name;
  const date = data.deadline
    ? moment(data.deadline).format("DD.MMMM, HH:mm")
    : null;
  const state = data.caseState ? <StateTag>{data.caseState}</StateTag> : null;
  const priority = data.priority ? <Priority>{data.priority}</Priority> : null;
  const listing = data.listing ? (
    <ListingItem mini data={data.listing} />
  ) : null;
  return (
    <div>
      <ListItem button onClick={() => onClick(data.id)}>
        <ListItemText>
          <Typography className={classes.header} variant="body1">
            {priority}
            {name}
          </Typography>
          <Typography variant="caption">{date}</Typography>
          {listing}
        </ListItemText>
        {state}
      </ListItem>
      <Divider />
    </div>
  );
};

const styles = {
  header: {
    display: "inline-flex",
    alignItems: "flex-end"
  }
};

CaseListItem.propTypes = {
  data: PropTypes.object.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(CaseListItem);
