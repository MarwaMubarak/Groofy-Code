import "./scss/groofyfooter.css";

const GroofyFooter = () => {
  return (
    <div className="footer-container">
      <h3 className="footer-contacts">
        <img src="/Assets/SVG/facebook.svg" alt="social" />
        <img src="/Assets/SVG/twitter.svg" alt="social" />
        <img src="/Assets/SVG/youtube.svg" alt="social" />
        <img src="/Assets/SVG/discord.svg" alt="social" />
        <img src="/Assets/SVG/instagram.svg" alt="social" />
      </h3>
      <h3 className="footer-welcome">
        Groofy Code is dedicated to fostering a thriving community of
        competitive programmers and providing a platform for learning, practice,
        and collaboration. We appreciate your respect for our intellectual
        property and the hard work of our contributors. Thank you for choosing
        Groofy Code for your competitive programming journey!
      </h3>
      <h3 className="footer-copyrights">
        © 2023 Groofy Code. All rights reserved. Competitive Programming
        Platform Groofy Code is a registered trademark of Groofy Code, Inc. Any
        unauthorized use, reproduction, or distribution of content or materials
        from this platform is strictly prohibited. All code submissions,
        problems, and editorial content are protected by copyright law.
      </h3>
    </div>
  );
};

export default GroofyFooter;
