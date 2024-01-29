const router = require("express").Router();
const calnController = require("../controllers/ClanController/ClanController");
const { verifyToken } = require('../middleware/verifyToken');

//create Clan
router.route('/clans/create').post(verifyToken, calnController.createClan);
router.get("/clans", calnController.getAllClans);
router.get('/clans/:clanId', calnController.getClanById);
router.route('/clans/update/:clanId').put(verifyToken, calnController.updateClanById);
router.route('/clans/delete/:clanId').delete(verifyToken, calnController.deleteClanById);


module.exports = router;