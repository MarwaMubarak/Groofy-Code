const mongoose = require('mongoose');
const { User } = require('../../models/userModel');

const userId = '654510ee30b6f24499b8e965'; 

// Find user by ID
User.findById(userId, (err, user) => {
  if (err) {
    console.error('Error:', err);
    // Handle the error, e.g., return an error response
  } else {
    if (user) {
      // User found, do something with the user data
      console.log('User:', user);
    } else {
      // User not found
      console.log('User not found');
      // Return a response indicating that the user was not found
    }
  }
});