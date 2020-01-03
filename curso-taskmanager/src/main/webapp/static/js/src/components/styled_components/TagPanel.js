import React from "react";
import { withStyles, Chip, Icon, Button } from "material-ui";
import PropTypes from "prop-types";

const TagPanel = props => {
  const { classes, tags, showTagListHandler, removeTagHandler } = props;

  return (
    <div className={classes.panel}>
      <div>
        {tags.map(t => <Chip key={t.id} color="primary" label={t.name} />)}
      </div>
      <Button
        onClick={() => showTagListHandler()}
        mini
        variant="fab"
        color="primary"
      >
        <Icon>edit</Icon>
      </Button>
    </div>
  );
};

const styles = {
  panel: {
    marginTop: "20px",
    display: "flex",
    flexWrap: "wrap",
    alignItems: "center"
  }
};

TagPanel.propTypes = {
  classes: PropTypes.object
};

export default withStyles(styles)(TagPanel);
