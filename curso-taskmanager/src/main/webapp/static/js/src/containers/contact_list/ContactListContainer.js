import React from "react";
import {
  contactListSelectors,
  contactListOperations
} from "es6!src/ducks/contact_list/index";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import ContactHeader from "es6!src/components/contact_list/ContactHeader";
import ContactItem from "es6!src/components/contact_list/ContactItem";

class ContactListContainer extends React.Component {
  constructor(props) {
    super(props);
    const { operations } = this.props;
    operations.fetchContacts();
  }

  render() {
    const { contacts, classes, fetched } = this.props;
    const contactList = fetched ? (
      <ScrollableContent>
        {contacts.map(item => {
          return <ContactItem key={item.id} data={item} />;
        })}
      </ScrollableContent>
    ) : null;
    return (
      <Panel>
        <ContactHeader />
        {contactList}
      </Panel>
    );
  }
}

const mapStateToProps = state => {
  const { fetchedSelector, contactsSelector } = contactListSelectors;
  return {
    contacts: contactsSelector(state),
    fetched: fetchedSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(contactListOperations, dispatch)
});

ContactListContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  contacts: PropTypes.array.isRequired,
  fetched: PropTypes.bool
};

export default connect(mapStateToProps, mapDispatchToProps)(
  ContactListContainer
);
