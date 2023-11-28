const { User, validateSignUp } = require('../models/userModel');
const bcrypt = require('bcryptjs');
const asyncHandler = require('express-async-handler');

module.exports.regiseterUser = asyncHandler(async(req, res) => {
    const { username, email, password } = req.body;

    const { error } = validateSignUp({ username, email, password });
    if (error) {
        return res.status(400).json({ error: error.details[0].message });
    }

    // Check if the user already exists in the database
    const existingUser = await User.findOne({ email });

    if (existingUser) {
        return res.status(400).json({ error: 'User already registered.' });
    }

    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);

    // Create a new user document with the hashed password
    const user = new User({
        username,
        email,
        password: hashedPassword,
    });

    await user.save();


    res.status(201).json({ message: 'Registration successful' });
});