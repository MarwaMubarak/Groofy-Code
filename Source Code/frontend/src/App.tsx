import {
  Route,
  Routes,
  BrowserRouter,
  Navigate,
  useNavigate,
} from "react-router-dom";
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

import { useDispatch, useSelector } from "react-redux";
import { userThunks } from "./store/actions";
import { useEffect } from "react";
import TestingSocket from "./pages/TestingSocket/TestingSocket";
import { WebSocketConnection } from "./components";

function App() {
  const loggedUser = useSelector((state: any) => state.auth.user);
  const dispatch = useDispatch();

  useEffect(() => {
    const getProfile = async () => {
      await dispatch(userThunks.getProfile() as any);
    };
    if (!loggedUser) {
      getProfile();
    }
  }, [dispatch, loggedUser]);

  if (!loggedUser && localStorage.getItem("token")) {
    return <div>Loading...</div>;
  }

  return (
    <BrowserRouter>
      <WebSocketConnection />
      <Routes>
        <Route
          path="/"
          element={loggedUser ? <Home /> : <Navigate to="/login" />}
        />
        <Route path="/login" element={<Login />} />
        <Route
          path="/signup"
          element={loggedUser ? <Navigate to="/" /> : <SignUp />}
        />
        <Route
          path="/profile/:username"
          element={loggedUser ? <Profile /> : <Navigate to="/login" />}
        />
        <Route
          path="/profile/edit"
          element={loggedUser ? <EditProfile /> : <Navigate to="/login" />}
        />
        <Route
          path="/play"
          element={loggedUser ? <Play /> : <Navigate to="/login" />}
        />
        <Route
          path="/clan"
          element={loggedUser ? <Clan /> : <Navigate to="/login" />}
        />
        <Route
          path="/news"
          element={loggedUser ? null : <Navigate to="/login" />}
        />
        <Route
          path="/help"
          element={loggedUser ? null : <Navigate to="/login" />}
        />
        <Route
          path="/game/:gameId"
          element={loggedUser ? <Match /> : <Navigate to="/login" />}
        />
        <Route
          path="/search"
          element={loggedUser ? <Search /> : <Navigate to="/login" />}
        />
        <Route
          path="/clan-search"
          element={loggedUser ? <ClanSearch /> : <Navigate to="/login" />}
        />
        <Route
          path="/user/message"
          element={loggedUser ? <Messaging /> : <Navigate to="/login" />}
        />
        <Route path="/testingsocket" element={<TestingSocket />} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
