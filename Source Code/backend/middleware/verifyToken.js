const jwt = require("jsonwebtoken")

function verifyToken(req, res, next) {
    const authToken = req.headers.authorization;
    if (authToken) {
        const token = authToken.split(" ")[1];
        try {
            const decodedPayload = jwt.verify(token, process.env.JWT_SECRETKEY);
            req.user = decodedPayload;
            next();
        } catch (error) {
            return res.status(401).json({ message: "Invalid token, access denied" });
        }
    } else {
        return res.status(401).json({ message: "No token provided, access denied" });
    }
}

module.exports = {
    verifyToken
}