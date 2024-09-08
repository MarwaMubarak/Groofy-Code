const axios = require("axios");

const options = {
  method: "POST",
  url: "https://judge0-ce.p.rapidapi.com/submissions",
  params: {
    base64_encoded: "true",
    wait: "true",
    fields: "*",
  },
  headers: {
    "content-type": "application/json",
    "Content-Type": "application/json",
    "X-RapidAPI-Key": "4558c4508fmsh09f2b1977054f11p122208jsn6ea5d5a1d31c",
    "X-RapidAPI-Host": "judge0-ce.p.rapidapi.com",
  },
  data: {
    language_id: 52,
    source_code:
      "I2luY2x1ZGUgPHN0ZGlvLmg+CgppbnQgbWFpbih2b2lkKSB7CiAgY2hhciBuYW1lWzEwXTsKICBzY2FuZigiJXMiLCBuYW1lKTsKICBwcmludGYoImhlbGxvLCAlc1xuIiwgbmFtZSk7CiAgcmV0dXJuIDA7Cn0=",
    stdin: "SnVkZ2Uw",
  },
};

try {
  const fetchRes = async () => {
    const response = await axios.request(options);
    console.log(response.data);
  };
  fetchRes();
} catch (error) {
  console.error(error);
}
