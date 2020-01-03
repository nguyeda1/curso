import React from "react";

import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import PropTypes from "prop-types";
import StyledInput from "es6!src/components/styled_components/StyledInput";
import { Button, InputAdornment, Icon } from "material-ui";

const withAsyncSearch = ({
  elementId,
  placeholder,
  search,
  defaultFetch,
  searchById,
  ownParams
}) => Component => {
  class WithAsyncSearch extends React.Component {
    constructor(props) {
      super(props);
      this.state = { [elementId]: "" };
    }

    getAdditionalParams() {
      const { additionalParams } = this.props;
      return additionalParams.map(p => {
        return p;
      });
    }

    handleQueryChange(event) {
      const { actions } = this.props;
      const query = event.target.value;
      this.setState({ [event.target.id]: query }, () => {
        query.length !== 0
          ? actions.search(...this.getAdditionalParams(), query)
          : actions.defaultFetch(...this.getAdditionalParams());
      });
    }

    render() {
      const { actions } = this.props;
      const query = this.state[elementId];
      const queryCouldBeId = query.length > 0 && !isNaN(query);

      const searchByIdButton = queryCouldBeId ? (
        <InputAdornment position="end">
          <Button
            onClick={() =>
              actions.searchById(...this.getAdditionalParams(), query)
            }
            size="small"
            color="primary"
          >
            Search by id
          </Button>
        </InputAdornment>
      ) : (
        <Icon>search</Icon>
      );

      const searchBar = (
        <StyledInput
          id={elementId}
          placeholder={placeholder}
          value={this.state[elementId]}
          onChange={this.handleQueryChange.bind(this)}
          endAdornment={searchByIdButton}
        />
      );
      return <Component {...this.props} searchBar={searchBar} />;
    }
  }

  const mapStateToProps = () => {
    return (state, ownProps) => {
      var additionalParams = ownParams
        ? ownParams.map(p => p(state, ownProps))
        : [];
      return {
        additionalParams
      };
    };
  };

  const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(
      {
        search,
        defaultFetch,
        searchById
      },
      dispatch
    )
  });

  return connect(mapStateToProps, mapDispatchToProps)(WithAsyncSearch);

  WithAsyncSearch.propTypes = {};
};
export default withAsyncSearch;
