import {
  Paginator,
  PaginatorCurrentPageReportOptions,
  PaginatorPageChangeEvent,
  PaginatorPageLinksOptions,
  PaginatorRowsPerPageDropdownOptions,
} from "primereact/paginator";
import { Dropdown } from "primereact/dropdown";
import { useEffect, useState } from "react";
import classes from "./scss/history.module.css";
import styles from "./scss/dropdown.module.css";
import { useDispatch, useSelector } from "react-redux";
import { gameThunks } from "../../../store/actions";

const History = () => {
  const dispatch = useDispatch();
  const games: any[] = useSelector((state: any) => state.game.games);
  const [first, setFirst] = useState<number>(0);
  const [rows, setRows] = useState<number>(5);
  const onPageChange = (event: PaginatorPageChangeEvent) => {
    setFirst(event.first);
    setRows(event.rows);
  };
  const PaginatorTemplate = {
    layout:
      "FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown ",
    RowsPerPageDropdown: (options: PaginatorRowsPerPageDropdownOptions) => {
      const dropdownOptions = [
        { label: 5, value: 5 },
        { label: 10, value: 10 },
      ];
      return (
        <>
          <span
            className="mx-1"
            style={{ color: "var(--text-color)", userSelect: "none" }}
          >
            Matches per page:{" "}
          </span>
          <Dropdown
            value={options.value}
            options={dropdownOptions}
            onChange={options.onChange}
            className={classes.paginator_dropdown}
            panelClassName={styles.paginator_dropdown_panel}
          />
        </>
      );
    },
    CurrentPageReport: (options: PaginatorCurrentPageReportOptions) => {
      return (
        <span
          style={{
            color: "var(--text-color)",
            userSelect: "none",
            width: "120px",
            textAlign: "center",
          }}
        >
          {options.first} - {options.last} of {options.totalRecords}
        </span>
      );
    },

    PageLinks: (options: PaginatorPageLinksOptions) => {
      return (
        <button
          className={
            classes.paginator_page_links +
            " " +
            (options.currentPage === options.page && classes.active)
          }
          onClick={options.onClick}
        >
          {options.page + 1}
        </button>
      );
    },
  };

  useEffect(() => {
    const getMatches = async (page: number, size: number) => {
      await dispatch(gameThunks.getAllUserGames(page) as any);
    };

    getMatches(first, rows);
  }, [dispatch, first, rows]);

  const formatDate = (date: string) => {
    const matchDate = new Date(date);
    const formattedDate = matchDate.toISOString().split("T")[0];
    return formattedDate;
  };

  return (
    <div className={classes.history_container}>
      <table>
        <thead>
          <tr>
            <th>Game Type</th>
            <th>Game Status</th>
            <th>Rating Change</th>
            <th>New Rating</th>
            <th>Game Date</th>
          </tr>
        </thead>
        <tbody>
          {games.length === 0 && (
            <tr className={classes.empty_history}>No games available</tr>
          )}
          {games.length > 0 &&
            games.map((game: any) => (
              <tr>
                <td style={{ width: "70px" }}>{game.gameType}</td>
                <td
                  style={{ width: "80px" }}
                  className={`${classes.state}
                  ${
                    game.gameResult === "Win"
                      ? classes.ww
                      : game.gameResult === "Lose"
                      ? classes.ll
                      : classes.dd
                  }
                `}
                >
                  {game.gameResult}
                </td>
                <td
                  className={`${classes.status} ${
                    game.ratingChange - parseInt("0") > 0
                      ? classes.w
                      : game.ratingChange - parseInt("0") === 0
                      ? classes.d
                      : classes.l
                  }`}
                >
                  {game.ratingChange > 0 ? "+" : null}
                  {game.ratingChange}
                </td>
                <td>{game.newRating}</td>
                <td>{formatDate(game.gameDate)}</td>
              </tr>
            ))}

          {/* <tr>
            <td>Solo Match</td>
            <td
              className={`${classes.status} ${
                "Accepted" === "Accepted" ? classes.w : classes.l
              }`}
            >
              Win
            </td>
            <td>24:52</td>
          </tr> */}
        </tbody>
      </table>
      <Paginator
        first={first}
        rows={rows}
        totalRecords={games.length}
        rowsPerPageOptions={[5, 10]}
        onPageChange={onPageChange}
        template={PaginatorTemplate}
        className={classes.paginator}
      />
    </div>
  );
};

export default History;
