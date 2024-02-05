const {
  User,
  validateSignUp,
  validateLoginEmail,
  validateLoginUserName,
  validateUpdateUser,
  validateChangePassword,
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
module.exports.regiseterUser = asyncHandler(async (req, res) => {
  try {
    const { username, email, password, firstname, lastname, country } =
      req.body;

    const { error } = validateSignUp({
      username,
      email,
      password,
      firstname,
      lastname,
      country,
    });

    if (error) {
      return res.status(400).json(unsuccessfulRes(error.details[0].message));
    }

    // Check if the email already exists in the database
    const existingEmail = await User.findOne({ email });
    // Check if the username already exists in the database
    const existingUserName = await User.findOne({ username });

    if (existingUserName) {
      return res
        .status(400)
        .json(unsuccessfulRes("Username is already taken."));
    }

    if (existingEmail) {
      return res
        .status(400)
        .json(unsuccessfulRes("Email is already registered."));
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
  } catch (error) {
    res.status(500).json(unsuccessfulRes("Internal server error"));
  }
});

/**----------------------------------------
 *  @description  Login User
 *  @rounter      /api/login 
 *  @method       POST
 *  @access       public 
 -----------------------------------------*/

module.exports.loginUser = asyncHandler(async (req, res) => {
  try {
    const { emailOrUserName, password } = req.body;

    const { errorEmail } = validateLoginEmail({ emailOrUserName, password });
    const { errorUserName } = validateLoginUserName({
      emailOrUserName,
      password,
    });
    if (errorEmail && errorUserName) {
      return res
        .status(400)
        .json(unsuccessfulRes("Invalid username/email or password."));
    }
    // Check if the user already exists in the database
    let userCan1, userCan2;
    if (!errorEmail) userCan1 = await User.findOne({ email: emailOrUserName });

    if (!errorUserName)
      userCan2 = await User.findOne({ username: emailOrUserName });

    let user = userCan1 || userCan2;
    if (!user) {
      return res
        .status(400)
        .json(unsuccessfulRes("Invalid username/email or password."));
    }

    const isPasswordMatch = await bcrypt.compare(password, user.password);
    if (!isPasswordMatch) {
      return res
        .status(400)
        .json(unsuccessfulRes("Invalid username/email or password."));
    }
    const token = user.generateAuthToken();

    user.isOnline = true;
    await user.save();

    const userData = user.toJSON();
    delete userData.password;
    userData.token = token;
    res.status(200).json(successfulRes("login successful", userData));
  } catch (error) {
    res.status(500).json(unsuccessfulRes("Internal server error"));
  }
});

/**----------------------------------------
 *  @description  Logout User
 *  @route        /api/logout 
 *  @method       POST
 *  @access       private (users only)
 -----------------------------------------*/
module.exports.logoutUser = asyncHandler(async (req, res) => {
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
 *  @route        /api/users/:userId
 *  @method       PUT
 *  @access       private (users only)
 -----------------------------------------*/
module.exports.updateUser = asyncHandler(async (req, res) => {
  try {
    const userId = req.user.id;
    const newBody = req.body;
    const { firstname, lastname, country, friends, bio, city, selectedBadges } =
      req.body;

    const { error } = validateUpdateUser({
      firstname,
      lastname,
      country,
      friends,
      bio,
      city,
    });
    const currUser = await User.findById(req.user.id);
    if (selectedBadges) {
      const validateSelectedBadges = selectedBadges.every((value) =>
        currUser.badges.includes(value)
      );
      if (!validateSelectedBadges) {
        return res.status(400).json(unsuccessfulRes("Select Valid Badges!"));
      }
    }
    if (error) {
      return res.status(400).json(unsuccessfulRes(error.details[0].message));
    }
    // update user
    const updatedUser = await User.findByIdAndUpdate(
      userId,
      { firstname, lastname, country, friends, bio, city, selectedBadges },
      {
        new: true,
      }
    ).select("-password");
    if (!updatedUser) {
      return res.status(404).json(unsuccessfulRes("User Not Found!"));
    }
    res.status(200).json(successfulRes("Updated successfully!", updatedUser));
  } catch (error) {
    res.status(500).json(unsuccessfulRes("Internal server error"));
  }
});

/**----------------------------------------
 *  @description  Change User Password
 *  @route        /api/users/change-password
 *  @method       POST
 *  @access       private
 -----------------------------------------*/
module.exports.changePassword = asyncHandler(async (req, res) => {
  try {
    const { currentPassword, password, confirmPassword } = req.body;

    const currentUser = await User.findOne({ _id: req.user.id });

    const isPasswordMatch = await bcrypt.compare(
      currentPassword,
      currentUser.password
    );
    if (!isPasswordMatch) {
      return res.status(400).json(unsuccessfulRes("Invalid password!"));
    }

    //validate changes
    const { error } = validateChangePassword({ password });
    if (error) {
      return res.status(400).json(unsuccessfulRes(error.details[0].message));
    }

    // update user
    if (password !== confirmPassword) {
      return res.status(400).json(unsuccessfulRes("Password not matched!"));
    }
    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);
    const updatedUser = await User.findByIdAndUpdate(
      req.user.id,
      { password: hashedPassword },
      { new: true }
    ).select("-password");

    if (!updatedUser) {
      return res.status(404).json(unsuccessfulRes("User Not Found!"));
    }
    return res
      .status(200)
      .json(successfulRes("Password Updated Successfully!", updatedUser));
  } catch (error) {
    res.status(500).json(unsuccessfulRes("Internal server error"));
  }
});

/**----------------------------------------
 *  @description  Get User Information by Username
 *  @route        /api/users/:username
 *  @method       GET
 *  @access       public
  -----------------------------------------*/
module.exports.getUserByUsername = asyncHandler(async (req, res) => {
  const { username } = req.params;
  // Find the user by the provided username
  const user = await User.findOne({ username });

  if (!user) {
    return res.status(404).json(unsuccessfulRes("User not found"));
  }

  // Exclude sensitive information like password
  const userData = user.toJSON();
  delete userData.password;

  res.status(200).json(successfulRes("User found", userData));
});

/**----------------------------------------
 *  @description  Search Users by Username Prefix
 *  @route        /api/users/search/:prefix
 *  @method       GET
 *  @access       public
 -----------------------------------------*/
module.exports.searchUsersByPrefix = asyncHandler(async (req, res) => {
  try {
    const { prefix } = req.params;

    // Validate if prefix is provided
    if (!prefix) {
      return res
        .status(400)
        .json(unsuccessfulRes("Prefix parameter is required"));
    }

    // Perform case-insensitive search for usernames starting with the given prefix
    const users = await User.find({
      username: { $regex: `^${prefix}`, $options: "i" },
    })
      .sort({ Trophies: -1 }) // Sort by Trophies in descending order
      .limit(5); // Limit the result to the top 5 users

    if (users.length === 0) {
      return res
        .status(404)
        .json(unsuccessfulRes("No users found with the provided prefix"));
    }

    // Extract desired information (username, country, photo) for each user
    const userInfo = users.map((user) => ({
      username: user.username,
      country: user.country,
      photo: user.photo,
    }));

    res.status(200).json(successfulRes("Users found", userInfo));
  } catch (error) {
    res.status(500).json(unsuccessfulRes("Internal server error"));
  }
});
