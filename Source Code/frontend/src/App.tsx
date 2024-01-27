import { Route, Routes, BrowserRouter, Navigate } from "react-router-dom";
import { Home, Login, SignUp, Match, Clan, Profile, Play } from "./pages";
import { ToastContainer } from "react-toastify";
import { useSelector } from "react-redux";
// import { GroofyFooter } from "./components";
function App() {
  const token = useSelector((state: any) => state.auth.token);
  console.log(token);
  return (
    <BrowserRouter>
      <ToastContainer position="top-center" />
      <Routes>
        <Route
          path="/"
          element={token != undefined ? <Home /> : <Navigate to="/login" />}
        />
        <Route
          path="/login"
          element={token != undefined ? <Navigate to="/" /> : <Login />}
        />
        <Route
          path="/signup"
          element={token != undefined ? <Navigate to="/" /> : <SignUp />}
        />
        <Route
          path="/profile"
          element={token != undefined ? <Profile /> : <Navigate to="/login" />}
        />
        <Route
          path="/play"
          element={token != undefined ? <Play /> : <Navigate to="/login" />}
        />
        <Route
          path="/clan"
          element={token != undefined ? <Clan /> : <Navigate to="/login" />}
        />
        <Route
          path="/news"
          element={token != undefined ? null : <Navigate to="/login" />}
        />
        <Route
          path="/help"
          element={token != undefined ? null : <Navigate to="/login" />}
        />
        <Route
          path="/match"
          element={token != undefined ? <Match /> : <Navigate to="/login" />}
        />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
