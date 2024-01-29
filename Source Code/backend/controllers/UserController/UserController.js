const {
    User,
    validateSignUp,
    validateLogin,
    validateUpdateUser,
} = require("../../models/userModel");
const bcrypt = require("bcryptjs");
const asyncHandler = require("express-async-handler");

const {
    successfulRes,
    unsuccessfulRes,
} = require("../../utilities/responseFormate");

/**----------------------------------------
 *  @description  Register New User
 *  @rounter      /api/register 
 *  @method       POST
 *  @access       public 
 -----------------------------------------*/

module.exports.regiseterUser = asyncHandler(async(req, res) => {
    const { username, email, password, firstname, lastname, country } = req.body;

    const { error } = validateSignUp({ username, email, password });
    if (error) {
        return res.status(400).json(unsuccessfulRes(error.details[0].message));
    }

    // Check if the email already exists in the database
    const existingEmail = await User.findOne({ email });
    // Check if the username already exists in the database
    const existingUserName = await User.findOne({ username });

    if (existingUserName) {
        return res.status(400).json(unsuccessfulRes("Username is already taken."));
    }

    if (existingEmail) {
        return res.status(400).json(unsuccessfulRes("Email is already registered."));
    }


    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);

    // Create a new user document with the hashed password
    const user = new User({
        username,
        email,
        password: hashedPassword,
        firstname: firstname,
        lastname: lastname,
        country: country,
    });

    await user.save();
    res
        .status(201)
        .json(successfulRes("Registration successful", "No data exist!"));
});

/**----------------------------------------
 *  @description  Login User
 *  @rounter      /api/login 
 *  @method       POST
 *  @access       public 
 -----------------------------------------*/

module.exports.loginUser = asyncHandler(async(req, res) => {
    const { email, password } = req.body;

    const { error } = validateLogin({ email, password });
    if (error) {
        return res.status(400).json(unsuccessfulRes(error.details[0].message));
    }
    // Check if the user already exists in the database
    const user = await User.findOne({ email });
    if (!user) {
        return res.status(400).json(unsuccessfulRes("Invalid Email or password."));
    }
    const isPasswordMatch = await bcrypt.compare(password, user.password);
    if (!isPasswordMatch) {
        return res.status(400).json(unsuccessfulRes("Invalid Email or password."));
    }
    const token = user.generateAuthToken();

    user.isOnline = true;
    await user.save();

    const userData = user.toJSON();
    delete userData.password;
    userData.token = token;
    res.status(200).json(successfulRes("login successful", userData));
});

/**----------------------------------------
 *  @description  Logout User
 *  @route        /api/logout 
 *  @method       POST
 *  @access       private (users only)
 -----------------------------------------*/
module.exports.logoutUser = asyncHandler(async(req, res) => {
    const userId = req.user.id;

    // Find the user by ID
    const user = await User.findById(userId);

    if (!user) {
        return res.status(404).json({ success: false, message: "User not found" });
    }

    // Update isOnline to false
    user.isOnline = false;
    await user.save();

    res.status(200).json({ success: true, message: "Logout successful" });
});


/**----------------------------------------
 *  @description  Update User
 *  @route        /user/update/:userId 
 *  @method       PUT
 *  @access       private (users only)
 -----------------------------------------*/
module.exports.updateUser = asyncHandler(async(req, res) => {

    try {
        const userId = req.params.userId;
        const newBody = req.body;
        const { password, firstname, lastname, country, friends, bio, city, selectedBadges } = req.body;

        // check if the authentication
        if (String(userId) !== String(req.user.id)) {
            return res
                .status(403)
                .json(
                    unsuccessfulRes(
                        "Unauthorized! You do not have permission to update user information."
                    )
                );
        }
        //validate changes
        const { error } = validateUpdateUser({ password, firstname, lastname, country, friends, bio, city });
        console.log(error)
        if (error) {
            return res.status(400).json(unsuccessfulRes(error.details[0].message));
        }
        // update user 
        if (newBody.password) {
            const salt = await bcrypt.genSalt(10);
            const hashedPassword = await bcrypt.hash(newBody.password, salt);
            newBody.password = hashedPassword;
        }
        const updatedUser = await User.findByIdAndUpdate(userId, newBody, { new: true });
        if (!updatedUser) {
            return res.status(404).json(unsuccessfulRes("User Not Found!"))
        }
        res.status(200).json(successfulRes("Updated successfully!", updatedUser));

    } catch (error) {
        console.error(error);
        res.status(500).json(unsuccessfulRes("Internal server error"))
    }

})