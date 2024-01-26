import { Route, Routes, BrowserRouter } from "react-router-dom";
import { Home, Login, SignUp, Match, Clan, Profile, Play } from "./pages";
import { ToastContainer } from "react-toastify";
// import { GroofyFooter } from "./components";
function App() {
  return (
    <BrowserRouter>
      <ToastContainer position="top-center" />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/play" element={<Play />} />
        <Route path="/clan" element={<Clan />} />
        <Route path="/news" />
        <Route path="/help" />
        <Route path="/match" element={<Match />} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
