import React from "react";
import PropTypes from "prop-types";
import HideableDescription from "es6!src/components/styled_components/HideableDescription";

class HideableDescriptionContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      more: false,
      showButton: true
    };
  }

  componentDidMount() {
    let element = document.getElementById("description_content");
    console.log(element.scrollWidth);
    console.log(element.offsetWidth);
    if (element.scrollWidth < element.offsetWidth) {
      this.setState({ showButton: false });
    }
  }

  handleShowMore() {
    const { more } = this.state;
    this.setState({ more: !more });
  }

  render() {
    const { description } = this.props;
    const { more, showButton } = this.state;
    return (
      <HideableDescription
        more={more}
        showMoreHandler={this.handleShowMore.bind(this)}
        showButton={showButton}
      >
        {description}
      </HideableDescription>
    );
  }
}

HideableDescriptionContainer.propTypes = {};

export default HideableDescriptionContainer;
