package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.Notification.LikeNotificationDTO;
import com.groofycode.GroofyCode.dto.PostDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Notification.LikeNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationType;
import com.groofycode.GroofyCode.model.Post.LikeModel;
import com.groofycode.GroofyCode.model.Post.PostModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.*;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationRepository notificationRepository;

    private final LikeNotificationRepository likeNotificationRepository;

    private final ModelMapper modelMapper;


    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate,
                       NotificationRepository notificationRepository, ModelMapper modelMapper, LikeRepository likeRepository,
                       LikeNotificationRepository likeNotificationRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
        this.likeRepository = likeRepository;
        this.modelMapper = modelMapper;
        this.likeNotificationRepository = likeNotificationRepository;
    }

    public ResponseEntity<Object> createPost(PostDTO postDTO) throws Exception {
        try {
            PostModel post = modelMapper.map(postDTO, PostModel.class);
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            post.setUser(userModel);
            post = postRepository.save(post);
            modelMapper.map(post, postDTO);
            postDTO.setIsLiked(0);
            postDTO.setPostUserId(userModel.getId());
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
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());
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
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());
            if (optionalPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("Post not found", null));
            }
            PostModel post = optionalPost.get();
            if (!post.getUser().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ResponseUtils.unsuccessfulRes("You are not allowed to delete this post", null));
            }
            likeNotificationRepository.deleteNotificationByPostId(id);
            likeRepository.deleteByPostId(id);
            postRepository.deleteById(id);
            return ResponseEntity.ok(ResponseUtils.successfulRes("Post deleted successfully", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> likePost(Long postId) throws Exception {
        try {
            // Get the current user who is liking the post
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());

            // Retrieve the post from the database
            Optional<PostModel> optionalPost = postRepository.findById(postId);
            if (optionalPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.unsuccessfulRes("Post not found", null));
            }
            PostModel post = optionalPost.get();
            
            // Check if the user has already liked the post
            LikeModel existingLike = likeRepository.findByUserIdAndPostId(currentUser.getId(), postId);
            if (existingLike != null) {
                // User has already liked the post, so remove the like
                likeRepository.delete(existingLike);
                post.setLikesCnt(post.getLikesCnt() - 1);
                postRepository.save(post);
                // Delete the associated like notification, if it exists
                NotificationModel likeNotification = likeNotificationRepository.findByPostAndSender(post, currentUser);
                if(likeNotification!=null)
                    notificationRepository.delete(likeNotification);


                return ResponseEntity.ok(ResponseUtils.successfulRes("Post unliked successfully", null));
            } else {
                // User has not liked the post, so add the like
                LikeModel like = new LikeModel();
                like.setUser(currentUser);
                like.setPost(post);
                // Save the like to the database
                likeRepository.save(like);
                post.setLikesCnt(post.getLikesCnt() + 1);
                postRepository.save(post);

                if (!post.getUser().getUsername().equals(currentUser.getUsername())) {
                    // Create and save the like notification
                    LikeNotificationModel likeNotification = new LikeNotificationModel();
                    likeNotification.setBody("liked your post");
                    likeNotification.setSender(currentUser);
                    likeNotification.setCreatedAt(new Date());
                    likeNotification.setReceiver(post.getUser());
                    likeNotification.setPost(post); // Set the associated post
                    likeNotification.setNotificationType(NotificationType.SEND_LIKE);
                    notificationRepository.save(likeNotification);
                    LikeNotificationDTO likeNotificationDTO = modelMapper.map(likeNotification, LikeNotificationDTO.class);
                    likeNotificationDTO.setSender(currentUser.getUsername());
                    likeNotificationDTO.setContent(post.getContent());
                    likeNotificationDTO.setImg(post.getUser().getPhotoUrl());
                    likeNotificationDTO.setPostId(post.getId());
                    Integer notifyCnt = notificationRepository.countUnRetrievedByReceiver(post.getUser());
                    likeNotificationDTO.setNotifyCnt(notifyCnt > 99 ? "99+" : notifyCnt.toString());
                    messagingTemplate.convertAndSendToUser(post.getUser().getUsername(), "/notification", likeNotificationDTO);

                    // @TODO -> Store buffered notifications in the database until it's opened
                }
                return ResponseEntity.ok(ResponseUtils.successfulRes("Post liked successfully", null));
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getUserPosts(Long userId) throws Exception {
        try {
            List<PostModel> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId);
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            List<PostDTO> postDTOs = posts.stream()
                    .map(p -> {
                        UserModel postUser = p.getUser();
                        PostDTO postDTO = modelMapper.map(p, PostDTO.class);
                        LikeModel existingLike = likeRepository.findByUserIdAndPostId(userModel.getId(), p.getId());
                        postDTO.setIsLiked(existingLike != null ? 1 : 0);
                        postDTO.setPostUserId(postUser.getId());
                        return postDTO;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtils.successfulRes("User posts retrieved successfully", postDTOs));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    public ResponseEntity<Object> getUserPostsPage(Long userId, int page,int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PostModel> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId,pageable);
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            List<PostDTO> postDTOs = posts.stream()
                    .map(p -> {
                        UserModel postUser = p.getUser();
                        PostDTO postDTO = modelMapper.map(p, PostDTO.class);
                        LikeModel existingLike = likeRepository.findByUserIdAndPostId(userModel.getId(), p.getId());
                        postDTO.setIsLiked(existingLike != null ? 1 : 0);
                        postDTO.setPostUserId(postUser.getId());
                        return postDTO;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseUtils.successfulRes("User posts retrieved successfully", postDTOs));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}