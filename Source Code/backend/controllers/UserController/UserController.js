const { User, validateSignUp , validateLogin } = require('../../models/userModel');
const bcrypt = require('bcryptjs');
const asyncHandler = require('express-async-handler');


/**----------------------------------------
 *  @description  Register New User
 *  @rounter      /api/register 
 *  @method       POST
 *  @access       public 
 -----------------------------------------*/


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
        return res.status(400).json({ error: error.details[0].message });
    }
    // Check if the user already exists in the database
    const user = await User.findOne({ email });
  
    if (!User) {
        return res.status(400).json({ error: 'User already registered.' });
    }  
    const isPasswordMatch = await bcrypt.compare( password , user.password)  
    if (!isPasswordMatch) {
      return res.status(400).json({ error: 'User already registered.' });
    }  
    const token = user.generateAuthToken(); 

    user.isOnline = true;
    await user.save();

    res.status(200).json({
      _id: user._id,
      photo: user.photo,
       token
      });

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