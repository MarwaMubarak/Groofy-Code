package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.PostDTO;
import com.groofycode.GroofyCode.model.LikeModel;
import com.groofycode.GroofyCode.model.PostModel;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.repository.LikeRepository;
import com.groofycode.GroofyCode.repository.PostRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostService(PostRepository postRepository, ModelMapper modelMapper, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<Object> createPost(PostDTO postDTO) throws Exception {
        try {
            PostModel post = modelMapper.map(postDTO, PostModel.class);
            UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            post.setUser(userModel);
            post = postRepository.save(post);
            modelMapper.map(post, postDTO);
            return ResponseEntity.ok(ResponseUtils.successfulRes("Post created successfully", postDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> updatePostById(Long id, PostDTO updatedPostDTO) throws Exception {
        try {
            Optional<PostModel> optionalPost = postRepository.findById(id);
            if (optionalPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("Post not found", null));
            }
            PostModel post = optionalPost.get();
            UserModel currentUser = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!post.getUser().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ResponseUtils.unsuccessfulRes("You are not allowed to update this post", null));
            }

            post.setContent(updatedPostDTO.getContent());
            post.setUpdatedAt(new Date());
            post = postRepository.save(post);
            modelMapper.map(post, updatedPostDTO);
            return ResponseEntity.ok(ResponseUtils.successfulRes("Post updated successfully", updatedPostDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> deletePostById(Long id) throws Exception {
        try {
            Optional<PostModel> optionalPost = postRepository.findById(id);
            UserModel currentUser = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (optionalPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("Post not found", null));
            }
            PostModel post = optionalPost.get();
            if (!post.getUser().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ResponseUtils.unsuccessfulRes("You are not allowed to delete this post", null));
            }
            postRepository.deleteById(id);
            return ResponseEntity.ok(ResponseUtils.successfulRes("Post deleted successfully", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public ResponseEntity<Object> likePost(Long postId) throws Exception {
        try {
            // Get the current user who is liking the post
            UserModel currentUser = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // Retrieve the post from the database
            Optional<PostModel> optionalPost = postRepository.findById(postId);

            if (optionalPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("Post not found", null));
            }
            PostModel post = optionalPost.get();
            // Check if the user has already liked the post
            Optional<LikeModel> existingLike = likeRepository.findByUserIdAndPostId(currentUser.getId(), postId);
            if (existingLike.isPresent()) {
                // User has already liked the post, so remove the like
                likeRepository.delete(existingLike.get());
                return ResponseEntity.ok(ResponseUtils.successfulRes("Post unliked successfully", null));
            } else {
                // User has not liked the post, so add the like
                LikeModel like = new LikeModel();
                like.setUser(currentUser);
                like.setPost(post);
                // Save the like to the database
                likeRepository.save(like);
                return ResponseEntity.ok(ResponseUtils.successfulRes("Post liked successfully", null));
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getUserPosts(Long userId) throws Exception {
        try {
            List<PostModel> posts = postRepository.findByUserId(userId);
            List<PostDTO> postDTOs = posts.stream()
                    .map(p -> modelMapper.map(p, PostDTO.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtils.successfulRes("User posts retrieved successfully", postDTOs));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
//    public PostDTO convertToDTO(PostModel post) {
//        PostDTO postDTO = new PostDTO();
//        postDTO.setContent(post.getContent());
//        postDTO.setLikes(likeService.getAllLikesForPost(post.getId()));
//        return postDTO;
//    }
}