import { useState } from "react";
import SingleFriend from "./SingleFriend/SingleFriend";
import "./scss/friends.css";
const Friends = () => {
  // Assuming you have a list of friend elements with class "friend"
  const friends: NodeListOf<Element> = document.querySelectorAll(
    ".singlefriend-container"
  );

  friends.forEach((friend: Element) => {
    friend.addEventListener("mouseenter", () => {
      // Calculate the position of the friend relative to the viewport
      const rect: DOMRect = friend.getBoundingClientRect();
      // Adjust the position of the floating menu based on the friend's position
      const floatingMenu: HTMLElement | null =
        friend.querySelector(".sf-popup");
      const menuHeight: number = floatingMenu ? floatingMenu.offsetHeight : 0;
      // Check if the friend is near the bottom of the viewport
      if (rect.bottom - 20 < menuHeight) {
        if (floatingMenu) {
          // floatingMenu.style.top = "auto";
          floatingMenu.style.bottom = "0"; // Adjust as needed
          console.log(window.innerHeight);
          console.log(rect.bottom);
          console.log(menuHeight);
          console.log("First");
        }
      } else {
        if (floatingMenu) {
          floatingMenu.style.top = "100%"; // Adjust as needed
          floatingMenu.style.bottom = "auto";
          console.log(window.innerHeight);
          console.log(rect.bottom);
          console.log(menuHeight);
          console.log("Second");
        }
      }
      // Show the floating menu
      if (floatingMenu) {
        floatingMenu.style.display = "block";
      }
    });
    friend.addEventListener("mouseleave", () => {
      // Hide the floating menu on mouse leave
      const floatingMenu: HTMLElement | null =
        friend.querySelector(".sf-popup");
      if (floatingMenu) {
        floatingMenu.style.display = "none";
      }
    });
  });
  const [friendsActive, setFriendsActive] = useState(false);
  return (
    <div className={`friends-container ${friendsActive}`}>
      <div
        className="friends-header"
        onClick={() => setFriendsActive((prev) => !prev)}
      >
        <div className="fh-img">
          <img src="/Assets/Images/Hazem Adel.jpg" alt="ProfilePhoto" />
          <h3>Friends</h3>
        </div>
        <img
          className={`fh-arrow ${friendsActive}`}
          src="/Assets/SVG/arrow-down.svg"
          alt="ExpandArrow"
        />
      </div>
      <div className="friends-content">
        <div className="fc-search">
          <img src="/Assets/SVG/search-icon.svg" alt="SearchIcon" />
          <input type="text" placeholder="Search Friends" />
        </div>
        <div className="fc-online">
          <span className="fc-box total active">Contacts (20)</span>
          <span className="fc-box online">Requests (5)</span>
        </div>
        <div className="fc-friends">
          <SingleFriend />
          <SingleFriend />
          <SingleFriend />
          <SingleFriend />
          <SingleFriend />
        </div>
      </div>
    </div>
  );
};

export default Friends;
