import { Route, Routes, BrowserRouter } from "react-router-dom";
import { Home, Login, SignUp, Match, Clan } from "./pages";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/profile" />
        <Route path="/clan" element={<Clan />} />
        <Route path="/news" />
        <Route path="/help" />
        <Route path="/match" element={<Match />} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
