import { useEffect, useRef, useState } from "react";
import { InputText } from "primereact/inputtext";
import { ClanResult, GroofyHeader, GroofyWrapper } from "../../components";
import { Button } from "primereact/button";
import classes from "./scss/clansearch.module.css";
import { useDispatch, useSelector } from "react-redux";
import clanThunks from "../../store/actions/clan-actions";
import { Toast } from "primereact/toast";
import { Skeleton } from "primereact/skeleton";
import { Dialog } from "primereact/dialog";
import { useNavigate } from "react-router-dom";

const ClanSearch = () => {
  const dispatch = useDispatch();
  const [createDialog, setCreateDialog] = useState<boolean>(false);
  const [clanName, setClanName] = useState<string>("");
  const [searchText, setSearchText] = useState<string>("");
  const [isTyping, setIsTyping] = useState<boolean>(false);
  const [searchTouched, setSearchTouched] = useState<boolean>(false);
  const [page, setPage] = useState<number>(0);
  const [size, setSize] = useState<number>(10);
  const totalClans: number = useSelector((state: any) => state.clan.totalClans);
  const searchedClans: any[] = useSelector((state: any) => state.clan.clans);
  const isLoading: boolean = useSelector((state: any) => state.clan.isLoading);
  const navigate = useNavigate();
  const toast = useRef<Toast>(null);

  useEffect(() => {
    const getClans = async () => {
      await dispatch(
        clanThunks.getSearchedClans(searchText, page, size) as any
      );
    };

    if (searchText === "") {
      setIsTyping(false);
      setSearchTouched(false);
      dispatch(clanThunks.clearSearchedClans() as any);
      return;
    }
    const typingTimeout = setTimeout(() => {
      getClans();
      setIsTyping(false);
    }, 1500);
    return () => clearTimeout(typingTimeout);
  }, [dispatch, searchText, page, size]);

  const handleSearchChange = (e: any) => {
    setSearchTouched(true);
    setIsTyping(true);
    setSearchText(e.target.value);
  };

  const createClan = async () => {
    if (clanName.trim() === "") {
      return { status: "failure", message: "Clan name cannot be empty" };
    }
    return await dispatch(clanThunks.createClan(clanName) as any);
  };

  return (
    <GroofyWrapper idx={3}>
      <Toast ref={toast} />
      <div className={classes.page_container}>
        <GroofyHeader />
        <Dialog
          header={"Create a clan"}
          visible={createDialog}
          onHide={() => setCreateDialog(false)}
        >
          <InputText
            placeholder="Clan Name"
            value={clanName}
            onChange={(e) => setClanName(e.target.value)}
          />
          <Button
            label={`${isLoading ? "Creating..." : "Create"}`}
            className={classes.create_btn}
            onClick={() => {
              createClan()
                .then((res: any) => {
                  if (res.status === "success") {
                    setCreateDialog(false);
                    toast.current?.show({
                      severity: "success",
                      summary: res.status,
                      detail: res.message,
                      life: 3000,
                    });
                    setTimeout(() => {
                      navigate("/clan");
                    }, 1000);
                  } else {
                    toast.current?.show({
                      severity: "error",
                      summary: res.status,
                      detail: res.message,
                      life: 3000,
                    });
                  }
                })
                .catch((error: any) => {
                  toast.current?.show({
                    severity: "error",
                    summary: error.status,
                    detail: error.message,
                    life: 3000,
                  });
                });
            }}
          />
        </Dialog>
        <div className={classes.clan_search}>
          <div className={classes.search_area}>
            <InputText
              placeholder="Search for clans..."
              className={classes.prime_input}
              value={searchText}
              onChange={handleSearchChange}
            />
            <i className="pi pi-search" />
            <Button
              label="Create Clan"
              icon="bi bi-plus-lg"
              className={classes.create_btn}
              onClick={() => setCreateDialog(true)}
            />
          </div>
          <div className={classes.clan_results}>
            {isTyping &&
              Array.from({ length: 6 }).map((_, idx) => (
                <Skeleton
                  key={idx}
                  width="388px"
                  height="190px"
                  borderRadius="6px"
                />
              ))}
            {searchedClans.length === 0 && !isTyping && searchTouched && (
              <div style={{ color: "#fff" }}>No clans found</div>
            )}
            {searchedClans.length === 0 && !isTyping && !searchTouched && (
              <div style={{ color: "#fff" }}>Search for clans</div>
            )}
            {searchedClans.length > 0 &&
              !isTyping &&
              searchedClans.map((clan: any) => (
                <ClanResult
                  key={clan.id}
                  clanId={clan.id}
                  clanName={clan.name}
                  members={clan.membersCount}
                  worldRank={clan.worldRank}
                  wins={clan.wins}
                  losses={clan.losses}
                  status={clan.requestStatus}
                />
              ))}
          </div>
          <div className={classes.paginator}>
            <Button icon="bi bi-chevron-left" className={classes.pag_btn} />
            {Array.from({ length: Math.ceil(totalClans / size) }).map(
              (_, idx) => (
                <Button
                  key={idx}
                  label={`${idx + 1}`}
                  className={classes.pag_btn}
                />
              )
            )}
            <Button icon="bi bi-chevron-right" className={classes.pag_btn} />
          </div>
        </div>
      </div>
    </GroofyWrapper>
  );
};

export default ClanSearch;
