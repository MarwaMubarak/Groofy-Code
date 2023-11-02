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
  scoreColor: string
  clickEvent: () => void;
}

// Achievemnt in profile props
export interface profileImageProps {
  profileImg: string;
  tagSkinImg: string;
  clickEvent: () => void;
}