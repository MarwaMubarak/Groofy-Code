const { Clan, createClanValidation, editClanValidation } = require("../models/clanModel");


/**----------------------------------------
 *  @description  Create New Clan
 *  @rounter      /api/clans/create
 *  @method       POST
 *  @access       Private (users only)
------------------------------------------*/
const createClan = async(req, res) => {
    console.log(req.body);
    try {
        const { errors } = createClanValidation(req.body);
        if (errors)
            return res.status(400).json({ error: errors.details[0].message });

        const clan = new Clan(req.body);
        await clan.save();
        res.status(201).json(clan);


    } catch (err) {
        res.status(500).json({ error: 'Internal Server Error' });

    }


}


/**----------------------------------------
 *  @description  Get all Clans
 *  @rounter      /api/clans
 *  @method       GET
 *  @access       public
------------------------------------------*/
const getAllClans = async(req, res) => {
    try {
        const clans = await Clan.find();
        res.json(clans);
    } catch (error) {
        res.status(500).json({ error: 'Internal Server Error' });
    }

}


/**----------------------------------------
 *  @description  Get Clan by ID
 *  @rounter      /api/clans/:clanId
 *  @method       GET
 *  @access       public
------------------------------------------*/
const getClanById = async(req, res) => {
    try {
        const clan = await Clan.findById(req.params.clanId);
        if (!clan)
            return res.status(404).json({ error: "Clan not found!" });
        res.json(clan);
    } catch (error) {
        res.status(500).json({ error: 'Internal Server Error' });

    }
}


/**----------------------------------------
 *  @description  Update Clan
 *  @rounter      /api/clans/update/:clanId
 *  @method       PUT
 *  @access       Private (users only)
------------------------------------------*/
const updateClanById = async(req, res) => {
    try {
        const { errors } = editClanValidation(req.body);
        // console.log(errors);
        if (errors)
            return res.status(400).json({ error: errors.details[0].message });

        const clan = await Clan.findByIdAndUpdate(req.params.clanId, req.body, { new: true });
        if (!clan)
            return res.status(404).json({ error: 'Clan not found' });
        res.json(clan);

    } catch (error) {
        console.log(error);
        res.status(500).json({ error: 'Internal Server Error' });

    }
}


/**----------------------------------------
 *  @description  Delete Clan
 *  @rounter      /api/clans/delete/:clanId
 *  @method       DELETE
 *  @access       Private (users only)
-----------------------------------------*/
const deleteClanById = async(req, res) => {
    try {
        const clan = await Clan.findByIdAndDelete(req.params.clanId);
        if (!clan) return res.status(404).json({ error: 'Clan not found' });

        res.json({ message: 'Clan deleted successfully' });
    } catch (error) {
        res.status(500).json({ error: 'Internal Server Error' });
    }
};


module.exports = {
    createClan,
    getAllClans,
    getClanById,
    updateClanById,
    deleteClanById,
};