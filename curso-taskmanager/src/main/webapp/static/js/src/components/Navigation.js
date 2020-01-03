import React from "react";
import {
  BottomNavigation,
  BottomNavigationAction,
  withStyles,
  Badge,
  Icon
} from "material-ui";
import PropTypes from "prop-types";
import RightBadge from "es6!src/components/styled_components/RightBadge";

const Navigation = props => {
  const { classes, path, redirectHandler, notificationCount } = props;
  return (
    <BottomNavigation value={path} showLabels>
      <BottomNavigationAction
        label="Cases"
        value="/case"
        icon={<Icon>apps</Icon>}
        onClick={() => redirectHandler("/case/list")}
      />
      <BottomNavigationAction
        label="My tasks"
        value="/task"
        icon={<Icon>format_list_bulleted</Icon>}
        onClick={() => redirectHandler("/task/list")}
      />
      <BottomNavigationAction
        label={
          notificationCount > 0 ? (
            <RightBadge
              color="secondary"
              classes={{ badge: classes.notification }}
              badgeContent={notificationCount}
            >
              Activities
            </RightBadge>
          ) : (
            "Activities"
          )
        }
        value="/activity"
        icon={<Icon>public</Icon>}
        onClick={() => redirectHandler("/activity/list")}
      />
      <BottomNavigationAction
        label="More"
        value="/more"
        icon={<Icon>more_horiz</Icon>}
        onClick={() => redirectHandler("/more")}
      />
    </BottomNavigation>
  );
};

const styles = theme => ({
  parent: {
    height: "10vh"
  },
  fixedNav: {
    position: "fixed",
    left: 0,
    bottom: 0,
    width: "100%"
  },
  notification: {
    top: "-30px",
    right: "10px"
  }
});

Navigation.propTypes = {
  path: PropTypes.string.isRequired,
  redirectHandler: PropTypes.func.isRequired,
  classes: PropTypes.object
};

export default withStyles(styles)(Navigation);
