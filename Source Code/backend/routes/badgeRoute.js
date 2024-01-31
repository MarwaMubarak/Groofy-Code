const router = require('express').Router();
const badgeController = require('../controllers/BadgeController/BadgeController')

router.post('/badges', badgeController.createBadge);
router.get('/badges', badgeController.getAllBadges);
router.get('/badges/:badgeId', badgeController.getBadgeById);
router.put('/badges/:badgeId', badgeController.updateBadgeById);
router.delete('/badges/:badgeId', badgeController.deleteBadgeById);

module.exports = router;