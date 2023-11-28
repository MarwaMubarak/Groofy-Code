import { title } from "process";

// button props
export interface btnProps {
  btnText: string;
  icnSrc?: string;
  clickEvent: () => void;
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
export interface SingleFollowCardProps{
  username: string;
  userImg: string;
}

// Single Follow Card Props
export interface EventCardProps{
  title: string;
  img: string;
  details: string;
  btn_title: string;
}

