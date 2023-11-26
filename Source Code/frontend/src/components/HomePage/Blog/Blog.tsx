import { useState } from "react";
import "./scss/blog.css";
import UserPopUp from "../../UserPopUP/UserPopUp";

const Blog = () => {
    const [likeActive, setLikeActive] = useState(false);
    return(
        <div className="blog-box align">
            <div className="b-box-background">
                <div className="b-user-info">
                    <div className="b-user-img">
                        <img src="/Assets/Images/Hazem Adel.jpg"></img>
                    </div>
                    <div className="b-user-name">
                        <h3>Hazem Adel</h3>
                        <span>3 hours ago.</span>
                    </div>
                    <UserPopUp/>
                </div>
                <div className="b-details">
                    <h3> Exciting Milestone Unlocked: 20 Consecutive Wins!</h3>
                    
                    <p>Overcoming challenges, mastering strategies, and pushing my limits—today, I'm thrilled to share the achievement of winning 20 games in a row! It's been an incredible journey filled with intense competition, lessons learned, and moments of pure adrenaline.</p>

                    <p>This badge represents not just a string of victories but a testament to dedication, perseverance, and the support of an amazing community. Huge thanks to my teammates, opponents, and everyone who has been a part of this gaming adventure.
                    Here's to the thrill of the game, the joy of victory, and the excitement of the next challenge! Ready for more epic gaming moments ahead. Let's keep the winning streak alive!</p>
                    <div className="b-details-img">
                        <img src="/Assets/Images/authPic.png"></img>
                    </div>
                </div>
                <div className="count-reactions">
                    <img className="single-reaction" src="/Assets/SVG/reaction-like.svg"/>
                    <img className="single-reaction" src="/Assets/SVG/reaction-love.svg"/>
                    <img className="single-reaction" src="/Assets/SVG/reaction-laugh.svg"/>
                    <img className="single-reaction" src="/Assets/SVG/reaction-angry.svg"/>
                    <span>3.2k</span>
                </div>
                <div className="reactions-section">
                    <div className="reactions-popup">
                        <div className="r-p-box">
                            <span>
                                Like
                            </span>
                            <img src="/Assets/SVG/reaction-like.svg"/>
                        </div>
                        <div className="r-p-box">
                            <span>
                                Love
                            </span>
                            <img src="/Assets/SVG/reaction-love.svg"/>
                        </div>
                        <div className="r-p-box">
                            <span>
                                Haha
                            </span>
                            <img src="/Assets/SVG/reaction-laugh.svg"/>
                        </div>
                        <div className="r-p-box">
                            <span>
                                Angry
                            </span>
                            <img src="/Assets/SVG/reaction-angry.svg"/>
                        </div>
                    </div>
                    <div className="react-button" onClick={()=>setLikeActive(state => !state)}>
                        {likeActive == true? 
                        <img src="/Assets/SVG/Like blue.svg" ></img>
                        :
                        <img src="/Assets/SVG/Like white.svg" ></img>
                        }
                        <span style={{color : likeActive == true?"#00A9FF": "black"}}>Like</span>
                    </div>
                    <div className="react-button">
                        <img src="/Assets/SVG/comment.svg"></img>
                        <span>Comment</span>
                    </div>
                    <div className="react-button">
                        <img src="/Assets/SVG/share.svg"></img>
                        <span>Share</span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Blog;
