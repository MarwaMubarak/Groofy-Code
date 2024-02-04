import { Route, Routes, BrowserRouter, Navigate } from "react-router-dom";
import {
  Home,
  Login,
  SignUp,
  Match,
  Clan,
  Profile,
  Play,
  EditProfile,
  Search,
  ClanSearch,
  Messaging,
} from "./pages";
import { useSelector } from "react-redux";

function App() {
  const user = useSelector((state: any) => state.auth.user);
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={user ? <Home /> : <Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />
        <Route
          path="/signup"
          element={user ? <Navigate to="/" /> : <SignUp />}
        />
        <Route
          path="/profile/:username"
          element={user ? <Profile /> : <Navigate to="/login" />}
        />
        <Route
          path="/profile/edit"
          element={user ? <EditProfile /> : <Navigate to="/login" />}
        />
        <Route
          path="/play"
          element={user ? <Play /> : <Navigate to="/login" />}
        />
        <Route
          path="/clan"
          element={user ? <Clan /> : <Navigate to="/login" />}
        />
        <Route path="/news" element={user ? null : <Navigate to="/login" />} />
        <Route path="/help" element={user ? null : <Navigate to="/login" />} />
        <Route
          path="/match"
          element={user ? <Match /> : <Navigate to="/login" />}
        />
        <Route
          path="/search"
          element={user ? <Search /> : <Navigate to="/login" />}
        />
        <Route
          path="/clan-search"
          element={user ? <ClanSearch /> : <Navigate to="/login" />}
        />
        <Route
          path="/user/message"
          element={user ? <Messaging /> : <Navigate to="/login" />}
        />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
