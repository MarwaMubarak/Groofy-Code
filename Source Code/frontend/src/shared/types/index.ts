import { Toast } from "primereact/toast";
import { RefObject } from "react";

// button props
export interface btnProps {
  btnText: string;
  icnSrc?: string;
  clickEvent?: () => void;
  btnType?: boolean;
  btnState?: boolean;
}
// testcase result props
export interface tcrProps {
  tcNum: number;
  icnSrc: string;
  tcStatus: boolean;
  detailsEvent?: () => void;
}

// game mode card props
export interface cardProps {
  cardImg: string;
  cardTitle: string;
  cardDescription: string;
  cardButtonTitle: string;
  clickEvent: () => void;
}

// Achievemnt in profile props
export interface achievementProps {
  achievementImg: string;
  achievementTitle: string;
  achievementName: string;
  achievementScore: number;
  achievementTrophy: string;
  scoreColor: string;
  clickEvent: () => void;
}

// Achievemnt in profile props
export interface profileImageProps {
  profileImg: string;
  tagSkinImg: string;
  clickEvent: () => void;
}

// Groofy Input props
export interface GroofyInputProps {
  giText: string;
  giPlaceholder: string;
  giType: string;
  giValue: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: (e: React.ChangeEvent<HTMLInputElement>) => void;
  errState: boolean;
  errMsg?: string;
}

// Notify Box Props

export interface NotifyBoxProps {
  nuImg: string;
  nusn: string;
  ntime: string;
}

// Single Friend Props
export interface SingleFriendProps {
  username: string;
  status: string;
  userImg: string;
}

// User Pop Up Props
export interface UserPopUpProps {
  username: string;
  skin?: string;
  status: string;
  gameType: string;
  userImg: string;
  clanName: string;
  clanImg: string;
  rankName: string;
  rankImg: string;
  badges: [string, string][];
}

// Profile Card Props
export interface ProfileCardProps {
  username: string;
  bio: string;
  followers: number;
  worldRank: number;
  level: number;
  percentage: number;
  skin?: string;
  userImg: string;
  clanName: string;
  clanImg: string;
  rankName: string;
  rankImg: string;
  badges: [string, string][];
}

// Single Follow Card Props
export interface SingleFollowCardProps {
  username: string;
  userImg: string;
}

// Single Follow Card Props
export interface EventCardProps {
  title: string;
  img: string;
  details: string;
  btn_title: string;
}

// Single Follow Card Props
export interface ActionButtonProps {
  img: string;
  count: number;
  clickEvent: () => void;
}

// Gamemode Props
export interface GamemodeProps {
  title: string;
  description?: string;
  type?: string;
  id?: string;
  img: string;
  clickEvent: () => void;
}

// Single Post props
export interface SinglePostProps {
  userid: string;
  postUser: string;
  postUserImg: string;
  postContent: string;
  postTime: string;
  postID: string;
  postLikesCnt: number;
  isLiked: boolean;
  isEdited: boolean;
}

// User Props
export interface UserProps {
  username?: string;
  displayName: string;
  email: string;
  password: string;
  confirmPassword?: string;
  country?: any;
}

// Posts Container Props
export interface PostsContainerProps {
  user: any;
  toast: RefObject<Toast>;
  self: boolean;
}

// Posts Props
export interface PostsProps {
  posts: any[];
  user: any;
  loggedUser: string;
}

// Groofy Textarea Props
export interface GroofyTAProps {
  taValue: string;
  changeHandler: (val: string) => void;
}

// PSocial Props
export interface PSocialProps {
  profileUser: any;
  profName: string;
  loggedUser: string;
  toast: RefObject<Toast>;
}

// PInfo Props
export interface PInfoProps {
  profileUser: any;
}

// Chat Props
export interface ChatProps {
  type?: string;
}

// Submission Props
export interface SubmissionProps {
  problemUrl?: string;
  language?: string;
  code: string;
}
