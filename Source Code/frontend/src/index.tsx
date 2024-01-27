import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import "./index.module.css";
import { Provider } from "react-redux";
import store from "./store";
import "primereact/resources/themes/lara-dark-blue/theme.css";
import "primereact/resources/primereact.min.css";
import "primeicons/primeicons.css";
import { Button } from "primereact/button";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <App />
      {/* <Button label="Click" /> */}
    </Provider>
  </React.StrictMode>
);
