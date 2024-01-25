const express = require("express");
const connectToDB = require("./config/connectToDB");
const helmet = require("helmet");
const hpp = require("hpp");
const cors = require("cors");
require("dotenv").config();

// Connect to MongoDB
connectToDB();



// Initializing express.js
const index = express();
index.use(express.json());

// Routes
index.use('/', require('./routes/userRoute'))
index.use('/', require('./routes/clanRoute'))
index.use('/', require('./routes/blogRoute'))
index.use('/', require('./routes/postRoute'))



// Security Measures
index.use(helmet());
index.use(hpp());


// Allowing cors policy
index.use(
    cors({
        origin: "*",
    })
);


// Starting up the server
const Port = process.env.PORT;
index.listen(Port, () => {
    console.log("Server is running successfully");
});