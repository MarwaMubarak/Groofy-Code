package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.PostDTO;
import com.groofycode.GroofyCode.dto.UserDTO;
import com.groofycode.GroofyCode.model.PostModel;
import com.groofycode.GroofyCode.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository; // Assuming you have a repository for posts

    public PostDTO createPost(PostDTO postDTO) {
        // Access the authenticated user through userDTO or SecurityContextHolder

        // Validate post data
        // You can implement createPostValidation method separately or use Spring's validation annotations

        // Convert DTO to entity
        PostModel post = new PostModel();
        post.setContent(postDTO.getContent());
//        post.setUser(userDTO); // Assuming user is associated with the post

        // Save the post
        PostModel savedPost = postRepository.save(post);

        // Convert saved entity back to DTO and return
        return convertToDTO(savedPost);
    }

    // You can define other service methods for updating, deleting, and retrieving posts

    // Utility method to convert entity to DTO
    private PostDTO convertToDTO(PostModel post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
//        postDTO.setUserId(post.getUser().getId());
        postDTO.setContent(post.getContent());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        // You can set other properties as needed
        return postDTO;
    }
}