const {
    Badge,
    validateBadge,
} = require('../../models/badgeModel');
const asyncHandler = require("express-async-handler");
const {
    successfulRes,
    unsuccessfulRes,
} = require("../../utilities/responseFormate");


/**----------------------------------------
 *  @description  create badge
 *  @rounter      /badge/creat
 *  @method       POST
 *  @access       private 
 -----------------------------------------*/
// Create a new badge
module.exports.createBadge = asyncHandler(async(req, res) => {

    try {
        const body = req.body;

        const { error } = validateBadge(body);

        if (error) {
            return res.status(400).json(unsuccessfulRes(error.details[0].message));
        }

        // found already
        const name = body.name;
        const found = await Badge.findOne({ name });
        console.log(found);
        if (found) {
            return res.status(400).json(unsuccessfulRes("Already Exist!"));
        }
        const newBadge = new Badge(body);
        const savedBadge = await newBadge.save();
        res.status(200).json(successfulRes("Created Successfully!", savedBadge));
    } catch (err) {
        console.error(err);
        res.status(500).json(unsuccessfulRes('Internal server error'));
    }
});


/**----------------------------------------
 *  @description  get all badge
 *  @rounter      /badge/all
 *  @method       GET
 *  @access       private 
 -----------------------------------------*/

// getAllBadges
module.exports.getAllBadges = asyncHandler(async(req, res) => {
    try {
        const allBadges = await Badge.find();
        res.status(200).status(200).json(allBadges);
    } catch (err) {
        console.error(err);
        res.status(500).json(unsuccessfulRes('Internal server error'));
    }
});

/**----------------------------------------
 *  @description  get badge by id
 *  @rounter      /badge/all
 *  @method       GET
 *  @access       private 
 -----------------------------------------*/
// Get a specific badge by ID
module.exports.getBadgeById = asyncHandler(async(req, res) => {
    const badgeId = req.params.badgeId;

    try {
        const foundBadge = await Badge.findById(badgeId);

        if (!foundBadge) {
            return res.status(404).json(unsuccessfulRes('Badge not found'));
        }

        res.status(200).status(200).json(successfulRes("Successfully!", foundBadge));
    } catch (err) {
        console.error(err);
        res.status(500).json(unsuccessfulRes('Internal server error'));
    }
});

/**----------------------------------------
 *  @description  Update a badge by ID
 *  @rounter      /badge/:badgeId
 *  @method       PUT
 *  @access       private 
 -----------------------------------------*/
// Update a badge by ID
module.exports.updateBadgeById = asyncHandler(async(req, res) => {
    const badgeId = req.params.badgeId;
    const updatedBadgeInfo = req.body;

    const { error } = validateBadge(updatedBadgeInfo);

    if (error) {
        return res.status(400).json({ message: error.details.map((detail) => detail.message) });
    }

    try {
        const updatedBadge = await Badge.findByIdAndUpdate(badgeId, updatedBadgeInfo, { new: true });

        if (!updatedBadge) {
            return res.status(404).json(unsuccessfulRes('Badge not found'));
        }

        res.status(200).json(successfulRes("Updated Successfully!", updatedBadge));
    } catch (err) {
        console.error(err);
        res.status(500).json(unsuccessfulRes('Internal server error'));
    }
});
/**----------------------------------------
 *  @description  Delete a badge by ID
 *  @rounter      /badge/:badgeId
 *  @method       Delete
 *  @access       private 
 -----------------------------------------*/

// Delete a badge by ID
module.exports.deleteBadgeById = asyncHandler(async(req, res) => {
    const badgeId = req.params.badgeId;

    try {
        const deletedBadge = await Badge.findByIdAndDelete(badgeId);

        if (!deletedBadge) {
            return res.status(404).json(unsuccessfulRes('Badge not found'));
        }

        res.status(200).json(successfulRes("Deleted Successfully!", deletedBadge));
    } catch (err) {
        console.error(err);
        res.status(500).json(unsuccessfulRes('Internal server error'));
    }
});