import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import Panel from "es6!src/components/styled_components/Panel";
import ScrollableContent from "es6!src/components/styled_components/ScrollableContent";
import { CardContent } from "material-ui";
import {
  tagListOperations,
  tagListSelectors
} from "es6!src/ducks/tag_list/index";
import { caseDetailGeneralSelectors } from "es6!src/ducks/case_detail/general/index";

import TagListHeader from "es6!src/components/tag_list/TagListHeader";
import TagListItem from "es6!src/components/tag_list/TagListItem";

class TagListContainer extends React.Component {
  constructor(props) {
    super(props);
    const { operations } = props;
    this.state = { selected: [] };
  }

  componentDidMount() {
    const { caseTags, operations } = this.props;
    operations.fetchList();
    this.setState({ selected: caseTags });
  }

  handleCheck(value) {
    const { selected } = this.state;
    if (this.selectedContainsTag(value)) {
      this.setState({ selected: selected.filter(s => s.id !== value.id) });
    } else {
      this.setState(prevState => ({
        selected: [...prevState.selected, value]
      }));
    }
  }

  handleSubmit() {
    const { operations, caseId, backHandler } = this.props;
    const { selected } = this.state;

    operations.editCaseTags(caseId, selected, backHandler);
  }

  selectedContainsTag(value) {
    const { selected } = this.state;
    var array = [...selected];
    return array.filter(c => c.id === value.id).length > 0;
  }

  render() {
    const { tags, backHandler } = this.props;
    const { selected } = this.state;

    return (
      <Panel>
        <TagListHeader
          backHandler={backHandler}
          submitHandler={this.handleSubmit.bind(this)}
        />
        <ScrollableContent>
          <CardContent>
            {tags.map(t => (
              <TagListItem
                checkHandler={this.handleCheck.bind(this)}
                checked={this.selectedContainsTag(t)}
                key={t.id}
                data={t}
              />
            ))}
          </CardContent>
        </ScrollableContent>
      </Panel>
    );
  }
}

const mapStateToProps = state => {
  const { tagListSelector, fetchedSelector } = tagListSelectors;
  return {
    caseTags: caseDetailGeneralSelectors.caseTagsSelector(state),
    tags: tagListSelector(state),
    fetched: fetchedSelector(state)
  };
};
const mapDispatchToProps = dispatch => ({
  operations: bindActionCreators(Object.assign({}, tagListOperations), dispatch)
});

TagListContainer.propTypes = {
  operations: PropTypes.object.isRequired,
  fetched: PropTypes.bool
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TagListContainer);
