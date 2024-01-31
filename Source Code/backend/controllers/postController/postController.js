const {
    Post,
    updatePostValidation,
    createPostValidation,
} = require("../../models/postModel");

const {
    successfulRes,
    unsuccessfulRes,
    isValidObjectId,
} = require("../../utilities/responseFormate");
const asyncHandler = require("express-async-handler");

const { ObjectId } = require('mongodb');

/**----------------------------------------
 *  @description  Create New Post
 *  @rounter      /api/post/create
 *  @method       POST
 *  @access       Private (users only)
------------------------------------------*/

const createPost = async(req, res) => {
    // Access the authenticated user through req.user
    const user = req.user;

    try {
        const { error } = createPostValidation(req.body);
        if (error) {
            return res
                .status(400)
                .json(unsuccessfulRes(error.message, { _data: error._original }));
        }

        // Create a new post
        const { content } = req.body;
        const post = new Post({
            content,
            user: user.id, // Associate the post with the logged-in user
        });

        await post.save();

        res.status(201).json(successfulRes("Post created successfully", post));
    } catch (error) {
        console.error(error);
        res.status(500).json(unsuccessfulRes(error));
    }
};

/**----------------------------------------
 *  @description  Update Post
 *  @rounter      /api/post/update/:postId
 *  @method       PUT
 *  @access       Private (users only)
------------------------------------------*/
const updatePostById = async(req, res) => {
    try {
        const { error } = updatePostValidation(req.body);
        if (error) {
            return res
                .status(400)
                .json(unsuccessfulRes(error.message, { _data: error._original }));
        }

        const post = await Post.findByIdAndUpdate(req.params.postId, req.body, {
            new: true,
        });
        if (!post) {
            ret = {
                status: "unsuccess",
                message: "Post not found",
                data: "No data exist!",
            };
            return res.status(404).json(ret);
        }

        // Check if the authenticated user has permission to update this post
        if (String(post.user) !== String(req.user.id)) {
            return res
                .status(403)
                .json(
                    unsuccessfulRes(
                        "Unauthorized. You do not have permission to update this post."
                    )
                );
        }
        const currentDate = new Date();
        post.updatedAt = currentDate;
        // Update the post if the user has permission
        const updatedPost = await post.save();
        res.json(successfulRes("Post updated successfully", updatedPost));
    } catch (error) {
        res.status(500).json(unsuccessfulRes(error));
    }
};

/**----------------------------------------
 *  @description  Delete Post
 *  @rounter      /api/post/delete/:postId
 *  @method       DELETE
 *  @access       Private (users only)
-----------------------------------------*/
const deletePostById = async(req, res) => {
    try {
        const post = await Post.findById(req.params.postId);
        if (!post) {
            return res.status(404).json({ error: "Post not found" });
        }
        // Check if the authenticated user has permission to delete this post
        if (String(post.user) !== String(req.user.id)) {
            return res
                .status(403)
                .json(
                    unsuccessfulRes(
                        "Unauthorized. You do not have permission to delete this post."
                    )
                );
        }

        // If the user has permission, delete the post
        await post.deleteOne();
        res.json(successfulRes("Post deleted successfully", "No data exist!"));
    } catch (error) {
        res.status(500).json(unsuccessfulRes(error));
    }
};

/**----------------------------------------
 *  @description  get Posts
 *  @rounter      /api/posts/:userId
 *  @method       get
 *  @access       public 
-----------------------------------------*/
const getUserPosts = async(req, res) => {
    try {
        const userId = req.params.userId;
        if (String(userId) !== String(req.user.id)) {
            return res
                .status(403)
                .json(
                    unsuccessfulRes(
                        "Unauthorized! You do not have permission to get the posts."
                    )
                );
        }

        // Fetch posts for the specified user
        const userPosts = await Post.find({ user: userId })
            .sort({ createdAt: -1 }) // Sort by createdDate in descending order
            // .populate("user", "username") // Populate user field with username only
            // .populate("like", "username"); // Populate like field with username only

        return res.json(successfulRes("All posts returned.", userPosts));
    } catch (error) {
        console.error(error);
        return res.status(500).json(unsuccessfulRes(error));
    }
};

/**----------------------------------------
 *  @description  Add or remove like for a specific post
 *  @router       /api/post/addLike/:postId
 *  @method       post
 *  @access       private 
-----------------------------------------*/
const addLike = async(req, res) => {
    try {
        const postId = req.params.postId;
        const currentUser = req.user;
        // Check if the post exists
        const post = await Post.findById(postId);
        if (!post) {
            return res.status(404).json(unsuccessfulRes("Post not found"));
        }

        // Check if the user has already liked the post
        const alreadyLikedIndex = post.likes.findIndex((like) =>
            like.user.equals(currentUser.id)
        );

        if (alreadyLikedIndex !== -1) {
            // User has already liked the post, so unlike
            post.likes.splice(alreadyLikedIndex, 1);
            await post.save();

            return res.json(successfulRes("User removed from the like list"));
        }

        // User has not liked the post, so add to the like list
        post.likes.push({ user: currentUser.id });
        await post.save();

        res.json(successfulRes("User added to the like list"));
    } catch (error) {
        console.error(error);
        res.status(500).json(unsuccessfulRes(error));
    }
};

module.exports = {
    createPost,
    updatePostById,
    deletePostById,
    getUserPosts,
    addLike,
};