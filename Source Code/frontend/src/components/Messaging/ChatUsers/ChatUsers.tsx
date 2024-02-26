import classes from "./scss/chatusers.module.css";
import { InputText } from "primereact/inputtext";
import { Badge } from "primereact/badge";

const ChatUsers = () => {
  return (
    <div className={classes.chats_container}>
      <div className={classes.chats_header}>
        <h3>Messages</h3>
        <i className="bi bi-pencil-square" />
      </div>
      <div className={classes.chats_search}>
        <InputText className={classes.search_input} placeholder="Search" />
        <i className="bi bi-search"></i>
      </div>
      <div className={classes.chats_users}>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.online}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Username</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>09:26 PM</span>
            <Badge className={classes.messages_cnt} value="2" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.online}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Username</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>09:26 PM</span>
            <Badge className={classes.messages_cnt} value="1" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.online}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Username</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>09:26 PM</span>
            <Badge className={classes.messages_cnt} value="1" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.online}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Username</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>09:26 PM</span>
            <Badge className={classes.messages_cnt} value="3" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.online}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Username</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>09:26 PM</span>
            <Badge className={classes.messages_cnt} value="6" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.online}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Username</h3>
              <p>
                WelcomeWelcomeWelcomeWelcomeWelcomeWelcomeWelcomeWelcomeWelcomeWelcomeWelcomeWelcomeWelcomeWelcomeWelcome
              </p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>09:26 PM</span>
            <Badge className={classes.messages_cnt} value="5" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.online}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Username</h3>
              <p>Hi there</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>05:26 AM</span>
            <Badge className={classes.messages_cnt} value="7" />
          </div>
        </div>
        <div className={classes.offline_users}>
          <i className="bi bi-wifi-off" />
          <span>offline</span>
        </div>

        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
        <div className={classes.chat_user}>
          <div className={classes.chat_user_info}>
            <div className={classes.chat_user_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="SearchProfilePic" />
              <div className={classes.offline}></div>
            </div>
            <div className={classes.chat_user_data}>
              <h3>Hazem Adel</h3>
              <p>Are you here?</p>
            </div>
          </div>
          <div className={classes.chat_user_status}>
            <span>10:26 PM</span>
            <Badge className={classes.messages_cnt} value="11" />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChatUsers;
