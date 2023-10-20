// button props
export interface btnProps {
  btnText: string;
  icnSrc: string;
  clickEvent: () => void;
}
// testcase result props
export interface tcrProps {
  tcNum: number;
  icnSrc: string;
  tcStatus: boolean;
  detailsEvent?: () => void;
}
