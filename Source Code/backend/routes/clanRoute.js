const router = require("express").Router();
const calnController = require("../controllers/ClanController/clanController");
const { verifyToken } = require("../middleware/verifyToken");

//create Clan
router.route("/clans/create").post(verifyToken, calnController.createClan);

router.route("/clans").get(verifyToken, calnController.getAllClans);

router.route("/clans/:clanId").get(verifyToken, calnController.getClanById);

router.route("/clans/update/:clanId").put(verifyToken, calnController.updateClanById);

router.route("/clans/delete/:clanId").delete(verifyToken, calnController.deleteClanById);

module.exports = router;
