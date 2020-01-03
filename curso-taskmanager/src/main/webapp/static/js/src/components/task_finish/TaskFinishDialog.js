import React from "react";
import {
  Dialog,
  DialogTitle,
  List,
  ListItem,
  ListItemText,
  Button,
  withStyles,
  FormControl,
  InputLabel,
  Input,
  CardContent
} from "material-ui";
import PropTypes from "prop-types";
import normalizeText from "es6!src/helpers/normalizeText";

class TaskFinishDialog extends React.Component {
  constructor(props) {
    super(props);
    this.state = { value: "", problemText: "" };
  }

  handleSelect(value) {
    this.setState({ value });
  }

  handleProblemChange(e) {
    this.setState({ problemText: e.target.value });
  }

  handleConfirm() {
    const { selectHandler } = this.props;
    const { value, problemText } = this.state;
    selectHandler(value, problemText);
  }

  validate() {
    const { value, problemText } = this.state;
    if (value !== "") {
      if (value != "DONE") {
        return problemText !== "";
      }
      return true;
    }
    return false;
  }

  render() {
    const { classes, selectHandler, onClose, states, ...other } = this.props;
    const confirm = (
      <Button
        disabled={!this.validate()}
        variant="contained"
        color="primary"
        onClick={() => this.handleConfirm()}
      >
        Confirm: {normalizeText(this.state.value)}
      </Button>
    );

    const problem =
      this.state.value !== "" && this.state.value !== "DONE" ? (
        <FormControl
          onChange={this.handleProblemChange.bind(this)}
          className={classes.formControl}
        >
          <InputLabel shrink htmlFor="problemText">
            Reason
          </InputLabel>
          <Input
            multiline
            rowsMax={3}
            value={this.state.problemText}
            id="problemText"
          />
        </FormControl>
      ) : null;
    return (
      <Dialog onClose={onClose} {...other}>
        <DialogTitle>Select finish state</DialogTitle>

        <List>
          {states.map(state => {
            return (
              <ListItem
                selected={this.state.value == state}
                button
                onClick={() => this.handleSelect(state)}
                key={state}
              >
                <ListItemText primary={normalizeText(state)} />
              </ListItem>
            );
          })}
        </List>
        <CardContent className={classes.content}>{problem}</CardContent>
        {confirm}
      </Dialog>
    );
  }
}

const styles = {
  content: { display: "flex", flexDirection: "column" }
};

TaskFinishDialog.propTypes = {
  selectHandler: PropTypes.func,
  classes: PropTypes.object
};

export default withStyles(styles)(TaskFinishDialog);
