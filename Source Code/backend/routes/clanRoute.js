const router = require("express").Router();
const calnController = require("../controllers/ClanController/clanController");
const { verifyToken } = require("../middleware/verifyToken");

//create Clan
router.route("/clans").post(verifyToken, calnController.createClan);

router.route("/clans").get(verifyToken, calnController.getAllClans);

router.route("/clans/:clanId").get(verifyToken, calnController.getClanById);

router.route("/clans/:clanId").put(verifyToken, calnController.updateClanById);

router.route("/clans/:clanId").delete(verifyToken, calnController.deleteClanById);

module.exports = router;