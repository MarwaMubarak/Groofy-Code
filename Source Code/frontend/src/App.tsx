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
import { useEffect, useState } from "react";
import TestingSocket from "./pages/TestingSocket/TestingSocket";
import { WebSocketConnection } from "./components";
import { userThunks, gameThunks } from "./store/actions";
import toast, { Toaster } from "react-hot-toast";
import classes from "./toast.module.css";
const messages = [
  "Searching for the perfect match...",
  "Looking for a worthy opponent...",
  "Scanning for challengers...",
  "Finding your next rival...",
  "Opponent search in progress...",
  "Hunting for a competitor...",
  "Matching you with a formidable foe...",
  "Connecting you to a challenger...",
  "Opponent hunt underway...",
  "Seeking a worthy adversary...",
];

const getRandomMessage = () =>
  messages[Math.floor(Math.random() * messages.length)];

function App() {
  const loggedUser = useSelector((state: any) => state.auth.user);
  const toastShow = useSelector((state: any) => state.toast.toastShow);
  const inQueue = useSelector((state: any) => state.game.inQueue);
  const dispatch = useDispatch();

  const [message, setMessage] = useState(getRandomMessage());
  const [toastId, setToastId] = useState<string | null>(null);
  const [exit, setExit] = useState(false);

  useEffect(() => {
    const getProfile = async () => {
      await dispatch(userThunks.getProfile() as any);
      await dispatch(gameThunks.checkRankedQueue() as any);
    };
    if (!loggedUser) {
      getProfile();
    }
  }, [dispatch, loggedUser]);

  console.log("QUEUE", inQueue);

  console.log("Logged User", loggedUser);

  useEffect(() => {
    const interval = setInterval(() => {
      setMessage(getRandomMessage());
    }, 5000); // Change message every 5 seconds

    if (toastShow || inQueue) {
      if (!toastId) {
        const id = toast.loading("QUEUEING", {
          position: "bottom-right",
          duration: Infinity,
        });
        setToastId(id);
        setExit(false);
      } else {
        toast.custom(
          (t) => (
            <div
              className={`${classes.toast_testing} ${exit ? classes.exit : ""}`}
            >
              <div className={classes.toast_header}>
                <h3>QUEUEING</h3>
              </div>
              <div className={classes.top}>
                <div className={classes.top_title}>
                  <i className="pi pi-spin pi-spinner"></i>
                  <h1>Matchmaking</h1>
                </div>
                <span>{message}</span>
              </div>
              <div
                className={classes.cancel_btn}
                onClick={() => {
                  setTimeout(() => {
                    toast.remove(t.id);
                  }, 1000);
                  dispatch(gameThunks.leaveQueue() as any);
                  setExit(true);
                }}
              >
                CANCEL
              </div>
            </div>
          ),
          {
            id: toastId, // Ensure it updates the existing toast
            duration: Infinity,
            position: "bottom-right",
          }
        );
      }
    } else if (toastId) {
      setTimeout(() => {
        toast.remove(toastId);
      }, 1000);
      setToastId(null);
    }

    return () => clearInterval(interval); // Cleanup on unmount
  }, [dispatch, inQueue, message, toastShow, toastId, exit]);

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
        <Route path="*" element={<Home />} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
