const router = require('express').Router();
const badgeController = require('../controllers/BadgeController/BadgeController')

router.post('/badge/create', badgeController.createBadge);
router.get('/badge/all', badgeController.getAllBadges);
router.get('/badge/:badgeId', badgeController.getBadgeById);
router.put('/badge/:badgeId', badgeController.updateBadgeById);
router.delete('/badge/:badgeId', badgeController.deleteBadgeById);

module.exports = router;