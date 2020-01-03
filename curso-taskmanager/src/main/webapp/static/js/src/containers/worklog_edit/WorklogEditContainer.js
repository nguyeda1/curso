import React from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import {
  worklogEditOperations,
  worklogEditSelectors
} from "es6!src/ducks/worklog_edit/index";
import {
  worklogDetailOperations,
  worklogDetailSelectors
} from "es6!src/ducks/worklog_detail/index";
import { navigationOperations } from "es6!src/ducks/navigation/index";
import WorklogEditHeader from "es6!src/components/worklog_edit/WorklogEditHeader";
import WorklogEditForm from "es6!src/components/worklog_edit/WorklogEditForm";

import WorklogCategoryListHeader from "es6!src/components/worklog_category_list/WorklogCategoryListHeader";
import WorklogCategoryListItem from "es6!src/components/worklog_category_list/WorklogCategoryListItem";

import VehicleListHeader from "es6!src/components/vehicle_list/VehicleListHeader";
import VehicleListItem from "es6!src/components/vehicle_list/VehicleListItem";

import WorklogTypeListHeader from "es6!src/components/worklog_type_list/WorklogTypeListHeader";
import WorklogTypeListItem from "es6!src/components/worklog_type_list/WorklogTypeListItem";

import Page from "es6!src/components/styled_components/Page";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import moment from "moment";
import SlideDialog from "es6!src/components/styled_components/SlideDialog";
import WorklogCategoryListContainer from "es6!src/containers/worklog_category_list/WorklogCategoryListContainer";
import { CardContent } from "material-ui";

class WorklogEditContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      dialogToggle: false,
      dialogType: "",
      from: moment().format("HH:mm"),
      to: moment()
        .add(30, "minutes")
        .format("HH:mm"),
      category: null,
      kmCount: 0,
      vehicle: null,
      toInvoice: false,
      type: null,
      description: "",
      task: { id: props.match.params.id },
      errors: {}
    };
  }

  componentDidMount() {
    const {
      operations,
      isProject,
      match,
      fetchedWorklog,
      editable,
      history
    } = this.props;
    if (match.params.fetchId) {
      operations.fetchById(match.params.fetchId);
      this.setState(
        {
          ...fetchedWorklog,
          task: { id: fetchedWorklog.task.id },
          from: moment(fetchedWorklog.from).format("HH:mm"),
          to: moment(fetchedWorklog.to).format("HH:mm")
        },
        () => {
          if (!editable) {
            operations.throwEditableError();
            history.goBack();
          }
          operations.switchWorklogType(fetchedWorklog.category.project);
        }
      );
    }
    operations.fetchCategories(isProject);
  }

  valueChangeHandler(event) {
    this.setState({ [event.target.id]: event.target.value });
  }

  invoiceToggle() {
    this.setState({ toInvoice: !this.state.toInvoice });
  }

  timeChangeHandler(event) {
    this.setState({ [event.target.id]: event.target.value });
  }

  dialogSelectHandler(key, value) {
    this.setState({ [key]: value, dialogToggle: false });
  }

  isProjectHandler() {
    const { operations, isProject } = this.props;
    operations.switchWorklogType(!isProject);
    this.setState({
      category: null,
      kmCount: 0,
      vehicle: null,
      toInvoice: false,
      type: null
    });
  }

  handleSubmit() {
    const { operations, match } = this.props;
    const {
      from,
      to,
      category,
      kmCount,
      vehicle,
      toInvoice,
      type,
      task,
      description
    } = this.state;
    const id = match.params.fetchId;
    if (this.validateForm()) {
      operations.editWorklog(id, {
        from,
        to,
        category,
        kmCount,
        vehicle,
        toInvoice,
        type,
        task,
        description
      });
    }
  }

  handleBack() {
    const { history } = this.props;
    history.goBack();
  }

  validateForm() {
    const {
      from,
      to,
      category,
      kmCount,
      vehicle,
      toInvoice,
      type,
      description
    } = this.state;

    const fromMoment = moment(from, "HH:mm");

    const toMoment = moment(to, "HH:mm");

    const categoryError = category ? undefined : "Category is required";

    const fromError = fromMoment.isBefore(toMoment)
      ? undefined
      : "Start must be smaller than End value";
    const toError = fromMoment.isBefore(toMoment)
      ? undefined
      : "Start must be smaller than End value";

    let kmError,
      vehicleError,
      typeError = null;
    if (category) {
      const { transport, invoicable } = category;
      kmError = transport
        ? !isNaN(kmCount)
          ? undefined
          : "KM should be a number"
        : undefined;
      vehicleError = transport
        ? vehicle
          ? undefined
          : "Vehicle is required"
        : undefined;

      typeError = invoicable
        ? type
          ? undefined
          : "Work type is required"
        : undefined;
    }

    this.setState({
      errors: {
        categoryError,
        fromError,
        toError,
        kmError,
        vehicleError,
        typeError
      }
    });
    if (
      categoryError ||
      fromError ||
      toError ||
      kmError ||
      vehicleError ||
      typeError
    ) {
      return false;
    }
    return true;
  }

  handleDialogToggle(dialogType) {
    this.setState({ dialogToggle: !this.state.dialogToggle, dialogType });
  }

  getDialogContent(dialogType) {
    const { vehicleList, typeList, categoryList } = this.props;
    switch (dialogType) {
      case "category": {
        return (
          <Page>
            <WorklogCategoryListHeader
              backHandler={this.handleDialogToggle.bind(this)}
            />
            <ScrollableContent>
              <CardContent>
                {categoryList.map(t => (
                  <WorklogCategoryListItem
                    selectHandler={this.dialogSelectHandler.bind(this)}
                    key={t.id}
                    data={t}
                  />
                ))}
              </CardContent>
            </ScrollableContent>
          </Page>
        );
      }
      case "vehicle": {
        return (
          <Page>
            <VehicleListHeader
              backHandler={this.handleDialogToggle.bind(this)}
            />
            <ScrollableContent>
              <CardContent>
                {vehicleList.map(t => (
                  <VehicleListItem
                    selectHandler={this.dialogSelectHandler.bind(this)}
                    key={t.id}
                    data={t}
                  />
                ))}
              </CardContent>
            </ScrollableContent>
          </Page>
        );
      }
      case "type": {
        return (
          <Page>
            <WorklogTypeListHeader
              backHandler={this.handleDialogToggle.bind(this)}
            />
            <ScrollableContent>
              <CardContent>
                {typeList.map(t => (
                  <WorklogTypeListItem
                    selectHandler={this.dialogSelectHandler.bind(this)}
                    key={t}
                    data={t}
                  />
                ))}
              </CardContent>
            </ScrollableContent>
          </Page>
        );
      }
    }
  }

  render() {
    const { categoryList, isProject } = this.props;
    const {
      dialogToggle,
      dialogType,
      from,
      to,
      category,
      kmCount,
      vehicle,
      toInvoice,
      type,
      description
    } = this.state;

    const dialog = (
      <SlideDialog open={dialogToggle} onClose={this.handleCategoriesToggle}>
        {this.getDialogContent(dialogType)}
      </SlideDialog>
    );

    return (
      <Page>
        <WorklogEditHeader
          backHandler={this.handleBack.bind(this)}
          submitHandler={this.handleSubmit.bind(this)}
        />
        <ScrollableContent>
          <WorklogEditForm
            values={{
              from,
              to,
              category,
              kmCount,
              vehicle,
              toInvoice,
              type,
              description,
              categoryList,
              isProject
            }}
            errors={this.state.errors}
            isProjectHandler={this.isProjectHandler.bind(this)}
            valueChangeHandler={this.valueChangeHandler.bind(this)}
            timeChangeHandler={this.timeChangeHandler.bind(this)}
            dialogToggleHandler={this.handleDialogToggle.bind(this)}
            invoiceToggleHandler={this.invoiceToggle.bind(this)}
          />
        </ScrollableContent>
        {dialogToggle ? dialog : null}
      </Page>
    );
  }
}

const mapStateToProps = state => {
  const {
    isProjectSelector,
    categoryListSelector,
    vehicleListSelector,
    typeListSelector
  } = worklogEditSelectors;
  const { worklogSelector, isEditableSelector } = worklogDetailSelectors;
  return {
    isProject: isProjectSelector(state),
    categoryList: categoryListSelector(state),
    vehicleList: vehicleListSelector(state),
    typeList: typeListSelector(state),
    fetchedWorklog: worklogSelector(state),
    editable: isEditableSelector(state)
  };
};

const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, worklogEditOperations, worklogDetailOperations),
    dispatch
  )
});

WorklogEditContainer.propTypes = {
  operations: PropTypes.object.isRequired
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorklogEditContainer);