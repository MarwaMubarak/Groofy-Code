import { useState } from "react";
import {
  Paginator,
  PaginatorCurrentPageReportOptions,
  PaginatorPageChangeEvent,
  PaginatorRowsPerPageDropdownOptions,
} from "primereact/paginator";
import { Dropdown } from "primereact/dropdown";
import { SinglePost } from "../..";
import { PostsProps } from "../../../shared/types";
import classes from "./scss/posts.module.css";

const Posts = (props: PostsProps) => {
  const [first, setFirst] = useState<number>(0);
  const [rows, setRows] = useState<number>(5);
  //   const user = useSelector((state: any) => state.auth.user);
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
        { label: 20, value: 20 },
        { label: 50, value: 50 },
      ];

      return (
        <>
          <span
            className="mx-1"
            style={{ color: "var(--text-color)", userSelect: "none" }}
          >
            Posts per page:{" "}
          </span>
          <Dropdown
            value={options.value}
            options={dropdownOptions}
            onChange={options.onChange}
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
  };
  return (
    <div className={classes.posts}>
      {props.posts && props.posts.length > 0 ? (
        <>
          {props.posts
            .slice(
              Math.min(first, props.posts.length),
              Math.min(first + rows, props.posts.length)
            )
            .map((post: any) => (
              <SinglePost
                key={post._id}
                userid={props.user._id}
                postUser={props.user.username}
                postUserImg={props.user.photo.url}
                postContent={post.content}
                postTime={post.createdAt}
                postID={post._id}
                postLikesCnt={post.likes.length}
                isEdited={post.createdAt !== post.updatedAt}
              />
            ))}
          <Paginator
            first={first}
            rows={rows}
            totalRecords={props.posts.length}
            rowsPerPageOptions={[5, 10, 20]}
            onPageChange={onPageChange}
            template={PaginatorTemplate}
          />
        </>
      ) : (
        <div className={classes.empty_posts}>No posts available.</div>
      )}
    </div>
  );
};

export default Posts;
