import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import Page from "es6!src/components/styled_components/Page";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import { CardContent } from "material-ui";
import {
  worklogCategoryListOperations,
  worklogCategoryListSelectors
} from "es6!src/ducks/worklog_category_list/index";

import { worklogEditSelectors } from "es6!src/ducks/worklog_edit/index";
import { caseDetailGeneralSelectors } from "es6!src/ducks/case_detail/general/index";

import WorklogCategoryListHeader from "es6!src/components/worklog_category_list/WorklogCategoryListHeader";
import WorklogCategoryListItem from "es6!src/components/worklog_category_list/WorklogCategoryListItem";

class WorklogCategoryListContainer extends React.Component {
  constructor(props) {
    super(props);
    const { operations } = props;
  }

  componentDidMount() {
    const { operations, isProject } = this.props;
    operations.fetchList(isProject);
  }

  render() {
    const { worklogCategories, backHandler, selectHandler } = this.props;

    return (
      <Page>
        <WorklogCategoryListHeader backHandler={backHandler} />
        <ScrollableContent>
          <CardContent>
            {worklogCategories.map(t => (
              <WorklogCategoryListItem
                selectHandler={selectHandler}
                key={t.id}
                data={t}
              />
            ))}
          </CardContent>
        </ScrollableContent>
      </Page>
    );
  }
}

const mapStateToProps = state => {
  const { isProjectSelector } = worklogEditSelectors;
  const {
    worklogCategoryListSelector,
    fetchedSelector
  } = worklogCategoryListSelectors;
  return {
    worklogCategories: worklogCategoryListSelector(state),
    fetched: fetchedSelector(state),
    isProject: isProjectSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(
    Object.assign({}, worklogCategoryListOperations),
    dispatch
  )
});

WorklogCategoryListContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  fetched: PropTypes.bool
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WorklogCategoryListContainer);
