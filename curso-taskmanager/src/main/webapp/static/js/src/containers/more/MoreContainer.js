import React from "react";
import PropTypes from "prop-types";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import LogoutContainer from "es6!src/containers/logout/LogoutContainer";
import ContactListContainer from "es6!src/containers/contact_list/ContactListContainer";

class MoreContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Panel>
        <LogoutContainer />
        <ContactListContainer />
      </Panel>
    );
  }
}

export default MoreContainer;
