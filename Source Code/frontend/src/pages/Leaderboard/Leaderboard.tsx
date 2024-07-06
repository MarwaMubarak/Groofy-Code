import { Toast } from "primereact/toast";
import React, { useEffect, useState } from "react";
import { GroofyHeader, SideBar } from "../../components";
import classes from "./scss/leaderboard.module.css";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import axios from "axios";
import {
  Paginator,
  PaginatorCurrentPageReportOptions,
  PaginatorPageChangeEvent,
  PaginatorPageLinksOptions,
  PaginatorRowsPerPageDropdownOptions,
} from "primereact/paginator";
import ReactCountryFlag from "react-country-flag";
interface Country {
  name: string;
  code: string;
}
const Leaderboard = () => {
  const token = localStorage.getItem("token");
  const [users, setUsers] = useState([]);
  const [first, setFirst] = useState<number>(0);
  const [rows, setRows] = useState<number>(20);
  const [totalRecords, setTotalRecords] = useState<number>(0);
  const countries: Country[] = [
    { name: "Australia", code: "AU" },
    { name: "Brazil", code: "BR" },
    { name: "China", code: "CN" },
    { name: "Egypt", code: "EG" },
    { name: "France", code: "FR" },
    { name: "Germany", code: "DE" },
    { name: "India", code: "IN" },
    { name: "Japan", code: "JP" },
    { name: "Spain", code: "ES" },
    { name: "United States", code: "US" },
  ];

  const onPageChange = (event: PaginatorPageChangeEvent) => {
    console.log(event);
    setFirst(event.first);
    setRows(event.rows);
  };
  const PaginatorTemplate = {
    layout:
      "FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport ",
    RowsPerPageDropdown: (options: PaginatorRowsPerPageDropdownOptions) => {
      return (
        <>
          <span
            className="mx-1"
            style={{ color: "var(--text-color)", userSelect: "none" }}
          >
            Users per page:{" "}
          </span>
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
    if (token) {
      const countAllUsers = async () => {
        const response = await axios.get(
          `http://localhost:8080/users/count-all-users`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        const data = response.data.body;
        setTotalRecords(data);
      };
      countAllUsers();
    }
  }, [token]);
  useEffect(() => {
    if (token) {
      const fetchLeaderboard = async () => {
        const response = await axios.get(
          `http://localhost:8080/users/leaderboard?page=${first}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        const data = response.data.body;
        setUsers(data);
      };
      fetchLeaderboard();
    }
  }, [first, token]);
  return (
    <div className={classes.leaderboard_container}>
      <SideBar idx={5} />
      <div className={classes.activity_section}>
        <GroofyHeader />
        <DataTable
          value={users}
          tableStyle={{
            minWidth: "50rem",
            marginTop: "30px",
          }}
        >
          <Column
            style={{ backgroundColor: "#303031", background: "#303031" }}
            field="rank"
            header="Rank"
            body={(rowData: any) => {
              return <span>{rowData.rank}</span>;
            }}
          />
          <Column
            style={{ backgroundColor: "#303031", background: "#303031" }}
            field="username"
            header="Username"
            body={(rowData: any) => {
              return (
                <div>
                  <ReactCountryFlag
                    countryCode={
                      countries.find(
                        (country) => country.name === rowData.country
                      )?.code || " "
                    }
                    svg
                    style={{
                      width: "1em",
                      height: "1em",
                      marginLeft: "8px",
                    }}
                    title={rowData.country || ""}
                  />
                  <span style={{ marginLeft: "8px" }}>{rowData.username}</span>
                </div>
              );
            }}
          />
          <Column
            style={{ backgroundColor: "#303031", background: "#303031" }}
            field="WLD"
            header="W / L / D"
            body={(rowData: any) => {
              return (
                <span>
                  {rowData.wins} / {rowData.losses} / {rowData.draws}
                </span>
              );
            }}
          />
          <Column
            style={{ backgroundColor: "#303031", background: "#303031" }}
            field="rating"
            header="Rating"
          />
        </DataTable>
        <Paginator
          first={first}
          rows={rows}
          totalRecords={totalRecords}
          onPageChange={onPageChange}
          template={PaginatorTemplate}
          className={classes.paginator}
          // style={{
          //   backgroundColor: "#18191a",
          // }}
        />
      </div>
    </div>
  );
};

export default Leaderboard;
