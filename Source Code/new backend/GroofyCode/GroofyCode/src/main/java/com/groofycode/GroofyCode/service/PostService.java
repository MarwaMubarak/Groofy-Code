package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.PostDTO;
import com.groofycode.GroofyCode.model.PostModel;
import com.groofycode.GroofyCode.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;


    public PostDTO createPost(PostDTO postDTO) {
        PostModel post = convertToEntity(postDTO);
        post.setCreatedAt(new Date());
        post = postRepository.save(post);
        return convertToDTO(post);
    }

    public PostDTO updatePostById(Long id, PostDTO updatedPostDTO) {
        try {
            Optional<PostModel> optionalPost = postRepository.findById(id);
            if (optionalPost.isEmpty()) {
                return null;
            }

            PostModel post = optionalPost.get();
            post.setContent(updatedPostDTO.getContent());
            post.setUpdatedAt(new Date());
            PostModel updatedPost = postRepository.save(post);

            return convertToDTO(updatedPost);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    private PostDTO convertToDTO(PostModel post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setContent(post.getContent());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
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
