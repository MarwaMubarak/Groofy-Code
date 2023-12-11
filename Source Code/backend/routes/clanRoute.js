const router = require("express").Router();
const calnController = require("../controllers/clanController");

//create Clan

router.route('/clans/create').post(verifyToken, calnController.createClan);
router.get("/clans", calnController.getAllClans);
router.get('/clans/:clanId', calnController.getClanById);
router.put('/clans/update/:clanId', calnController.updateClanById);
router.route('/clans/delete/:clanId').delete(verifyToken, calnController.deleteClanById);


module.exports = router;