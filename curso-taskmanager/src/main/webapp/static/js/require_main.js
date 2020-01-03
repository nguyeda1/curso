require.config({
  waitSeconds: 200,
  paths: {
    axios: "vendor/axios-0.18.0",
    es6: "vendor/es6",
    babel: "vendor/babel-5.8.34.min",
    babel_polyfill: "vendor/babel-polyfill",
    "material-ui": "vendor/material-ui",
    moment: "vendor/moment-2.22.2",
    "prop-types": "vendor/prop-types",
    ramda: "vendor/ramda-0.25.0",
    react: "vendor/react",
    "react-dom": "vendor/react-dom",
    "react-redux": "vendor/react-redux-5.0.7",
    "react-router-dom": "vendor/react-router-dom",
    "redux-logger": "vendor/redux-logger-3.0.6.min",
    "redux-thunk": "vendor/redux-thunk-2.3.0",
    redux: "vendor/redux",
    reselect: "vendor/reselect-3.0.1",
    "rxjs-adapter": "vendor/rxjs-adapter",
    "styled-components": "vendor/styled-components-3.3.3.min"
  },
  shim: {
    babel: {
      deps: ["babel_polyfill"]
    }
  }
});

require(["react", "react-dom", "es6!Main"], function(React, ReactDOM, Main) {
  ReactDOM.render(React.createElement(Main), document.getElementById("root"));
  //    var div = document.getElementById("root");
  //    div.innerHTML += 'Extra stuff';
});
