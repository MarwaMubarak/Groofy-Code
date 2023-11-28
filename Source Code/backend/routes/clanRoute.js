const router = require("express").Router();
const calnController = require("../controllers/clanController");

//create Clan
router.post("/clans/create", calnController.createClan);
router.get("/clans", calnController.getAllClans);
router.get('/clans/:clanId', calnController.getClanById);
router.put('/clans/update/:clanId', calnController.updateClanById);
router.delete('/clans/delete/:clanId', calnController.deleteClanById);


module.exports = router;