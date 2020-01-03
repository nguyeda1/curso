import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { getCurrentUser } from "es6!src/helpers/localStorage";
import { contains, equals, find, propEq } from "ramda";
import {
  worklogDetailSelectors,
  worklogDetailOperations
} from "es6!src/ducks/worklog_detail/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import { bindActionCreators } from "redux";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import SlideDialog from "es6!src/components/styled_components/SlideDialog";
import Page from "es6!src/components/styled_components/Page";
import WorklogDetailHeader from "es6!src/components/worklog_detail/WorklogDetailHeader";
import WorklogDetailContent from "es6!src/components/worklog_detail/WorklogDetailContent";

class WorklogDetailContainer extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    const { operations, match } = this.props;
    const id = match.params.id;
    operations.fetchById(id);
  }

  handleEdit() {
    const { operations, match } = this.props;
    const id = match.params.id;
    operations.redirect("/worklog/edit/" + id);
  }

  handleBack() {
    const { history, operations } = this.props;
    history.goBack();
  }

  structureData(data) {
    const {
      from,
      to,
      category,
      kmCount,
      vehicle,
      type,
      description,
      toInvoice,
      user,
      task,
      date
    } = data;
    const { invoicable, transport } = category;
    let tableRows = [];
    if (invoicable) {
      tableRows.push({ name: "KM", value: kmCount });
      tableRows.push({ name: "Vehcile", value: vehicle });
    }
    if (transport) {
      tableRows.push({ name: "To invoice", value: toInvoice ? "Yes" : "No" });
      tableRows.push({ name: "Work type", value: type });
    }

    return { tableRows, from, to, category, description, user, task, date };
  }

  render() {
    const { data, fetched, worklogId, operations, editable } = this.props;
    const general = fetched ? (
      <WorklogDetailContent data={this.structureData(data)} />
    ) : null;

    return (
      <Page>
        <WorklogDetailHeader
          editable={editable}
          editHandler={this.handleEdit.bind(this)}
          backHandler={this.handleBack.bind(this)}
        />
        <ScrollableContent>{general}</ScrollableContent>
      </Page>
    );
  }
}

WorklogDetailContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  data: PropTypes.object.isRequired,
  fetched: PropTypes.bool
};

const mapStateToProps = state => {
  const {
    worklogSelector,
    fetchedSelector,
    isEditableSelector
  } = worklogDetailSelectors;
  return {
    data: worklogSelector(state),
    fetched: fetchedSelector(state),
    editable: isEditableSelector(state)
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, worklogDetailOperations, navigationOperations),
    dispatch
  )
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorklogDetailContainer);
