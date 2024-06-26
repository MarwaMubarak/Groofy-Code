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

import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import TestingSocket from "./pages/TestingSocket/TestingSocket";
import { WebSocketConnection } from "./components";
import { userThunks, gameThunks } from "./store/actions";
import toast, { Toaster } from "react-hot-toast";
import classes from "./toast.module.css";

function App() {
  const loggedUser = useSelector((state: any) => state.auth.user);
  const toastShow = useSelector((state: any) => state.toast.toastShow);
  const inQueue = useSelector((state: any) => state.game.inQueue);
  const dispatch = useDispatch();

  useEffect(() => {
    const getProfile = async () => {
      await dispatch(userThunks.getProfile() as any);
      await dispatch(gameThunks.checkQueue() as any);
    };
    if (!loggedUser) {
      getProfile();
    }
  }, [dispatch, loggedUser]);

  console.log("QUEUE", inQueue);

  useEffect(() => {
    const getToast = () =>
      toast.custom(
        (t) => (
          <div className={classes.toast_testing}>
            <div className={classes.top}>
              <i className="pi pi-spin pi-spinner"></i>
              <span>Finding opponent...</span>
            </div>
            <div
              className={classes.cancel_btn}
              onClick={() => {
                dispatch(gameThunks.leaveQueue() as any);
                toast.dismiss(t.id);
              }}
            >
              Cancel
            </div>
          </div>
        ),
        {
          duration: Infinity,
          position: "bottom-right",
        }
      );
    if (toastShow || inQueue) {
      getToast();
    }
  }, [dispatch, inQueue, toastShow]);

  if (!loggedUser && localStorage.getItem("token")) {
    return <div>Loading...</div>;
  }

  return (
    <BrowserRouter>
      <WebSocketConnection />
      <Toaster />
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
