function successfulRes(_massage, _data) {
  return {
    status: "success",
    message: _massage,
    body: _data,
  };
}

function unsuccessfulRes(_massage, { _data } = {}) {
  if (!_data) _data = "No data exist!";

  return {
    status: "failure",
    message: _massage,
    body: _data,
  };
}

function isValidObjectId(id) {

  const objectIdPattern = /^[0-9a-fA-F]{24}$/;
  return objectIdPattern.test(id);
}

module.exports = {
  successfulRes,
  unsuccessfulRes,
  isValidObjectId
};
