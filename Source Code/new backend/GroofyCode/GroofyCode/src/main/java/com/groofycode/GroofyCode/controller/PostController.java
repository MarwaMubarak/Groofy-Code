package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.PostDTO;
import com.groofycode.GroofyCode.model.LikeModel;
import com.groofycode.GroofyCode.service.LikeService;
import com.groofycode.GroofyCode.service.PostService;
import com.groofycode.GroofyCode.utilities.ResponseModel;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Object> createPost(@RequestBody @Valid PostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Object> updatePostById(@PathVariable Long postId, @RequestBody @Valid PostDTO postDTO) {
        return postService.updatePostById(postId, postDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Object> deletePostById(@PathVariable Long postId) {
        return postService.deletePostById(postId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserPosts(@PathVariable Long userId) {
        return postService.getUserPosts(userId);
    }

    // Endpoint to like a post
    @PostMapping("/{postId}/like")
    public ResponseEntity<Object> likePost(@PathVariable Long postId) {
        return postService.likePost(postId);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("; "));
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ResponseUtils.unsuccessfulRes(errorMessage.toString(), null));
    }
}

