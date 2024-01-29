const mongoose = require('mongoose');
const Joi = require('joi');
const JoiPassComplex = require('joi-password-complexity');
const Jwt = require('jsonwebtoken');
const BadgeSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        unique: true,
        minlength: 4,
        maxlength: 100,

    },
    photo: {
        type: Object,
        default: {
            url: "https://images.are.na/eyJidWNrZXQiOiJhcmVuYV9pbWFnZXMiLCJrZXkiOiI4MDQwOTc0L29yaWdpbmFsX2ZmNGYxZjQzZDdiNzJjYzMxZDJlYjViMDgyN2ZmMWFjLnBuZyIsImVkaXRzIjp7InJlc2l6ZSI6eyJ3aWR0aCI6MTIwMCwiaGVpZ2h0IjoxMjAwLCJmaXQiOiJpbnNpZGUiLCJ3aXRob3V0RW5sYXJnZW1lbnQiOnRydWV9LCJ3ZWJwIjp7InF1YWxpdHkiOjkwfSwianBlZyI6eyJxdWFsaXR5Ijo5MH0sInJvdGF0ZSI6bnVsbH19?bc=0",
            publicID: null,
        },
    },
    description: {
        type: String,
        required: true,
        minlength: 4,
        maxlength: 1000,

    },

});

const Badge = mongoose.model('Badge', BadgeSchema);

const validateBadge = (badge) => {
    const schema = Joi.object({
        name: Joi.string().trim().min(4).max(100).required(),
        photo: Joi.object({
            url: Joi.string().uri(),
            publicID: Joi.string().allow(null),
        }),
        description: Joi.string().trim().min(4).max(1000).required(),
    });

    return schema.validate(badge, { abortEarly: false }); // abortEarly: false returns all validation errors instead of stopping on the first one
};


module.exports = {
    Badge,
    validateBadge,
}