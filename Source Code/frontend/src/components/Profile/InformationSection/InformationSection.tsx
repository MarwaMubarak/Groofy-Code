import "./scss/information.css";

const InformationSection = () => {
  return (
    <div className="solver-info">
        <h1>Rokba</h1>
        <div className="Address-info">
          <h4>Hazem Adel, </h4>
          <h4>Giza, </h4>
          <h4>Egypt</h4>
        </div>
        <div className="details-section">
          <h3 className="details-title">World Ranks: </h3>
          <h3 className="details-info"> #9720 </h3>
        </div>
        <div className="details-section">
          <h3 className="details-title">Last Seen: </h3>
          <h3 className="details-info" style={{color : '#52BB00'}}> Online </h3>
        </div>
        <div className="details-section">
          <h3 className="details-title">Friends of: </h3>
          <h3 className="details-info"> 512  </h3>
          <h3 className="details-title"> solvers </h3>
        </div>
        <div className="details-section">
          <h3 className="details-title"> Since </h3>
          <h3 className="details-info"> 8/5/2020 </h3>
        </div>
        <div className="details-achievement">
            <div className="data-container">
                <div className="data-container-column">
                    <h3>Total Matches</h3>
                    <div className="data-container-field">
                      <h3 className="data-field-info">800</h3>
                    </div>
                    <h3>Wins</h3>
                    <div className="data-container-field">
                      <h3 className="data-field-info">733</h3>
                    </div>
                    <h3>Draws</h3>
                    <div className="data-container-field">
                      <h3 className="data-field-info">15</h3>
                    </div>
                </div>
                <div className="data-container-column">
                    <h3>Highest Trophies</h3>
                    <div className="data-container-field">
                      <h3 className="data-field-info">5030</h3>
                    </div>
                    <h3>Loses</h3>
                    <div className="data-container-field">
                      <h3 className="data-field-info">52</h3>
                    </div>
                    <h3>K/D/A</h3>
                    <div className="data-container-field">
                      <h3 className="data-field-info">14</h3>
                    </div>
                </div>
            </div>
        </div>
    </div>
  );
};

export default InformationSection;
