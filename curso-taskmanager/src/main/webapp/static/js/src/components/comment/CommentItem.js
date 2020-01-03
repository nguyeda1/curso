import React from "react";
import {
  SnackbarContent,
  withStyles,
  Typography,
  ListItem,
  ListItemText,
  Avatar
} from "material-ui";
import Item from "es6!src/components/styled_components/Item";
import SquareAvatar from "es6!src/components/styled_components/SquareAvatar";
import PropTypes from "prop-types";
import moment from "moment";

const CommentItem = props => {
  const { data, classes, commentIsMine } = props;
  const isMine = commentIsMine(data);

  const avatar = isMine ? null : <SquareAvatar>U</SquareAvatar>;

  const date = data.createdOn
    ? moment(data.createdOn).format("DD.MMMM, HH:mm")
    : null;

  const author = isMine ? "Me" : data.assignee.username;

  const createInfo = author + " on " + date;
  return (
    <ListItem className={classes.container}>
      {avatar}
      <ListItemText className={classes.noPadding}>
        <div className={isMine ? classes.mine : classes.other}>
          <Typography className={classes.wordBreak} variant="body1">
            {data.text}
          </Typography>
        </div>
        <Typography className={classes.createInfo} variant="caption">
          {createInfo}
        </Typography>
      </ListItemText>
    </ListItem>
  );
};

const styles = theme => ({
  container: { paddingLeft: 10, paddingBotton: 5, alignItems: "center" },
  noPadding: {
    padding: 0
  },
  createInfo: { textAlign: "right" },
  wordBreak: { wordWrap: "break-word", color: "inherit" },
  other: {
    borderRadius: "25px 15px 15px 0px",
    backgroundColor: "#efefef",
    boxShadow: "inherit",
    color: "black",
    padding: "10px 15px 15px 10px"
  },
  mine: {
    borderRadius: "15px 25px 0px 15px",
    backgroundColor: theme.palette.primary.dark,
    padding: "10px 15px 15px 10px",
    color: "white"
  }
});

CommentItem.propTypes = {
  data: PropTypes.object.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(CommentItem);
