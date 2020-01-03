import React, { Component } from "react";
import { connect } from "redux";

const withInfiniteScroller = ({
  fetchData,
  refreshData,
  getData,
  lastFetch
}) => Component => {
  class WithInfiniteScroller extends Component {
    constructor(props) {
      super(props);
    }

    componentDidMount() {
      const { actions } = this.props;
      actions.fetch();
    }

    render() {
      const { data } = this.props;

      return <Component items={data} />;
    }
  }

  return WithInfiniteScroller;
};

export default withInfiniteScroller;
