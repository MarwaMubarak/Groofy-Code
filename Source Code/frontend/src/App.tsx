import { Route, Routes, BrowserRouter } from "react-router-dom";
import { Home } from "./pages";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/profile" />
        <Route path="/clan" />
        <Route path="/login" />
        <Route path="/signup" />
        <Route path="/news" />
        <Route path="/help" />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
