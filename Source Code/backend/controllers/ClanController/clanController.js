const {
  Clan,
  createClanValidation,
  editClanValidation,
} = require("../../models/clanModel");
const {
  successfulRes,
  unsuccessfulRes,
  isValidObjectId,
} = require("../../utilities/responseFormate");
/**----------------------------------------
 *  @description  Create New Clan
 *  @rounter      /api/clans/create
 *  @method       POST
 *  @access       Private (users only)
------------------------------------------*/
const createClan = async (req, res) => {
  try {
    const { error } = createClanValidation(req.body);
    if (error)
      return res.status(400).json(unsuccessfulRes(error.details[0].message));

    req.body.leader = req.user.id;

    // Check if the clan name is already taken
    const existingClan = await Clan.findOne({ name: req.body.name });
    if (existingClan) {
      return res.status(400).json(unsuccessfulRes("Clan name already exists. Please choose a different name."));
    }

    const clan = new Clan(req.body);
    await clan.save();
    res.status(201).json(successfulRes("Clan created successfully!", clan));
  } catch (err) {
    res.status(500).json(unsuccessfulRes("Internal Server Error"));
  }
};

/**----------------------------------------
 *  @description  Get all Clans
 *  @rounter      /api/clans
 *  @method       GET
 *  @access       public
------------------------------------------*/
const getAllClans = async (req, res) => {
  try {
    const clans = await Clan.find();

    res.json(successfulRes("Clans retrieved successfully", clans));
  } catch (error) {
    res.status(500).json(unsuccessfulRes("Internal Server Error"));
  }
};

/**----------------------------------------
 *  @description  Get Clan by ID
 *  @rounter      /api/clans/:clanId
 *  @method       GET
 *  @access       public
------------------------------------------*/
const getClanById = async (req, res) => {
  try {
    const clan = await Clan.findById(req.params.clanId);

    if (!clan) {
      return res.status(404).json(unsuccessfulRes("Clan not found!"));
    }

    res.json(successfulRes("Clan retrieved successfully", clan));
  } catch (error) {
    res.status(500).json(unsuccessfulRes("Internal Server Error"));
  }
};

/**----------------------------------------
 *  @description  Update Clan
 *  @rounter      /api/clans/update/:clanId
 *  @method       PUT
 *  @access       Private (users only)
------------------------------------------*/
const updateClanById = async (req, res) => {
  try {
    const { error } = editClanValidation(req.body);

    if (error) {
      return res.status(400).json(unsuccessfulRes(error.details[0].message));
    }

    // Check if the new name is already taken by another clan
    const existingClan = await Clan.findOne({ name: req.body.name });

    if (existingClan) {
      return res
        .status(400)
        .json(unsuccessfulRes("Clan name already exists. Please choose a different name."));
    }

    // Check if the user is the leader of the clan
    const clan = await Clan.findOne({ _id: req.params.clanId, leader: req.user.id });

    if (!clan) {
      return res.status(403).json(unsuccessfulRes("You do not have permission to update this clan"));
    }

    const updatedClan = await Clan.findByIdAndUpdate(req.params.clanId, req.body, { new: true });

    if (!updatedClan) {
      return res.status(404).json(unsuccessfulRes("Clan not found"));
    }

    res.json(successfulRes("Clan updated successfully", updatedClan));
  } catch (error) {
    res.status(500).json(unsuccessfulRes("Internal Server Error"));
  }
};


/**----------------------------------------
 *  @description  Delete Clan
 *  @rounter      /api/clans/delete/:clanId
 *  @method       DELETE
 *  @access       Private (users only)
-----------------------------------------*/
const deleteClanById = async (req, res) => {
  try {
    // Check if the user is the leader of the clan
    const clan = await Clan.findOne({ _id: req.params.clanId, leader: req.user.id });

    if (!clan) {
      return res.status(403).json(unsuccessfulRes("You do not have permission to delete this clan"));
    }

    const deletedClan = await Clan.findByIdAndDelete(req.params.clanId);

    if (!deletedClan) {
      return res.status(404).json(unsuccessfulRes("Clan not found"));
    }

    res.json(successfulRes("Clan deleted successfully", deletedClan));
  } catch (error) {
    res.status(500).json(unsuccessfulRes("Internal Server Error"));
  }
};


module.exports = {
  createClan,
  getAllClans,
  getClanById,
  updateClanById,
  deleteClanById,
};
