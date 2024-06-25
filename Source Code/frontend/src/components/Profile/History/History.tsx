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
  const userMatches: any[] = useSelector(
    (state: any) => state.match.userMatches
  );
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
            <th>Match Type</th>
            <th>State</th>
            <th>Status</th>
            <th>Played at</th>
          </tr>
        </thead>
        <tbody>
          {userMatches.length === 0 && (
            <tr className={classes.empty_history}>No matches available</tr>
          )}
          {userMatches.length > 0 &&
            userMatches.map((match: any) => (
              <tr>
                <td>Solo Match</td>
                <td className={`${classes.state} ${classes.f}`}>
                  {match.state}
                </td>
                <td
                  className={`${classes.status} ${
                    match.status === "Accepted" ? classes.w : classes.l
                  }`}
                >
                  {match.status}
                </td>
                <td>{formatDate(match.createdAt)}</td>
              </tr>
            ))}
        </tbody>
      </table>
      <Paginator
        first={first}
        rows={rows}
        totalRecords={20}
        rowsPerPageOptions={[5, 10]}
        onPageChange={onPageChange}
        template={PaginatorTemplate}
        className={classes.paginator}
      />
    </div>
  );
};

export default History;
