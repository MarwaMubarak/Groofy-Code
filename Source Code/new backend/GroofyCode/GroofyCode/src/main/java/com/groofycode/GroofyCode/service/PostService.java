package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.LikeDTO;
import com.groofycode.GroofyCode.dto.PostDTO;
import com.groofycode.GroofyCode.model.LikeModel;
import com.groofycode.GroofyCode.model.PostModel;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.repository.LikeRepository;
import com.groofycode.GroofyCode.repository.PostRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseModel;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeRepository likeRepository;

    public ResponseEntity<Object> createPost(PostDTO postDTO) {
        try {
            PostModel post = convertToEntity(postDTO);
            post.setCreatedAt(new Date());
            UserModel userModel = userService.getCurrentUser();
            post.setUser(userModel);
            post = postRepository.save(post);
            PostDTO createdPostDTO = convertToDTO(post);

            return ResponseEntity.ok(ResponseUtils.successfulRes("Post created successfully", createdPostDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));
        }
    }


    public ResponseEntity<Object> updatePostById(Long id, PostDTO updatedPostDTO) {
        try {
            Optional<PostModel> optionalPost = postRepository.findById(id);
            if (optionalPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("This post id not found", null));
            }

            PostModel post = optionalPost.get();
            UserModel currentUser = userService.getCurrentUser();

            if (!post.getUser().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ResponseUtils.unsuccessfulRes("You are not allowed to update this post", null));
            }

            post.setContent(updatedPostDTO.getContent());
            post.setUpdatedAt(new Date());
            PostModel updatedPost = postRepository.save(post);

            return ResponseEntity.ok(ResponseUtils.successfulRes("The post updated successfully", convertToDTO(updatedPost)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));
        }
    }


    public ResponseEntity<Object> deletePostById(Long id) {
        try {
            Optional<PostModel> optionalPost = postRepository.findById(id);
            if (optionalPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("This post id not found", null));
            }

            PostModel post = optionalPost.get();
            UserModel currentUser = userService.getCurrentUser();

            if (!post.getUser().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ResponseUtils.unsuccessfulRes("You are not allowed to delete this post", null));
            }

            postRepository.deleteById(id);

            return ResponseEntity.ok(ResponseUtils.successfulRes("The post deleted successfully", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));
        }
    }


    public ResponseEntity<Object> likePost(Long postId) {
        try {
            // Retrieve the post from the database
            Optional<PostModel> optionalPost = postRepository.findById(postId);
            if (optionalPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("This post id not found", null));
            }
            PostModel post = optionalPost.get();

            // Get the current user who is liking the post
            UserModel currentUser = userService.getCurrentUser();

            // Check if the user has already liked the post
            Optional<LikeModel> existingLike = likeRepository.findByUserIdAndPostId(currentUser.getId(), postId);
            if (existingLike.isPresent()) {
                // User has already liked the post, so remove the like
                likeRepository.delete(existingLike.get());
                return ResponseEntity.ok(ResponseUtils.successfulRes("The like removed successfully", null));
            } else {
                // User has not liked the post, so add the like
                LikeModel like = new LikeModel();
                like.setUser(currentUser);
                like.setPost(post);
                // Save the like to the database
                like = likeRepository.save(like);
                return ResponseEntity.ok(ResponseUtils.successfulRes("The like added successfully", likeService.convertToDto(like)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));
        }
    }


    public ResponseEntity<Object> getUserPosts(Long userId) {
        try {
            // Fetch posts by user ID from the repository
            List<PostModel> posts = postRepository.findByUserId(userId);
            // Convert PostModel list to PostDTO list
            List<PostDTO> postDTOs = posts.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ResponseUtils.successfulRes("User posts retrieved successfully", postDTOs));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));
        }
    }

    public PostDTO convertToDTO(PostModel post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setUserId(post.getUser().getId());
        postDTO.setContent(post.getContent());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        postDTO.setLikes(likeService.getAllLikesForPost(post.getId()));
        // Set other properties
        return postDTO;
    }


    private PostModel convertToEntity(PostDTO postDTO) {
        PostModel post = new PostModel();
        post.setContent(postDTO.getContent());
        post.setUpdatedAt(post.getUpdatedAt());
        post.setCreatedAt(post.getCreatedAt());
        return post;
    }
}
