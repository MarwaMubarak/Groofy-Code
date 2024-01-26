function successfulRes(_massage, _data) {
  return {
    status: "success",
    message: _massage,
    data: _data,
  };
}

function unsuccessfulRes(_massage, { _data } = {}) {
  if (!_data) _data = "No data exist!";

  return {
    status: "unsuccess",
    message: _massage,
    data: _data,
  };
}

module.exports = {
  successfulRes,
  unsuccessfulRes,
};
