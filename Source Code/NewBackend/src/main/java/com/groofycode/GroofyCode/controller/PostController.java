package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.PostDTO;
import com.groofycode.GroofyCode.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Object> createPost(@RequestBody @Valid PostDTO postDTO) throws Exception {
        return postService.createPost(postDTO);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Object> updatePostById(@PathVariable Long postId, @RequestBody @Valid PostDTO postDTO) throws Exception {
        return postService.updatePostById(postId, postDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Object> deletePostById(@PathVariable Long postId) throws Exception {
        return postService.deletePostById(postId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserPosts(@PathVariable Long userId) throws Exception {
        return postService.getUserPosts(userId);
    }

    @GetMapping("/{userId}/pagination")
    public ResponseEntity<Object> getUserPostsPage(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {
        return postService.getUserPostsPage(userId, page,size);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Object> likePost(@PathVariable Long postId) throws Exception {
        return postService.likePost(postId);
    }
}

