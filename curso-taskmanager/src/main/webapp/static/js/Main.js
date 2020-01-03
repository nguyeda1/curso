import { Provider } from "react-redux";
import { HashRouter, hashHistory } from "react-router-dom";
import React from "react";
import App from "es6!src/components/App";

import store from "es6!src/helpers/store";

import { createMuiTheme, MuiThemeProvider } from "material-ui";

const theme = createMuiTheme({
  overrides: {
    MuiIcon: {
      root: {
        overflow: "visible"
      }
    }
  }
});

const Main = () => (
  <Provider store={store}>
    <HashRouter history={hashHistory}>
      <React.Fragment>
        <MuiThemeProvider muiTheme={theme}>
          <App />
        </MuiThemeProvider>
      </React.Fragment>
    </HashRouter>
  </Provider>
);

export default Main;
