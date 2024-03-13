package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.model.LikeModel;
import com.groofycode.GroofyCode.repository.LikeRepository;
import com.groofycode.GroofyCode.dto.LikeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public List<LikeDTO> getAllLikesForPost(Long postId) {
        List<LikeModel> likeModels = likeRepository.findAllByPostId(postId);
        return likeModels.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LikeDTO convertToDto(LikeModel likeModel) {
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setUserId(likeModel.getUser().getId());
        likeDTO.setLikedAt(likeModel.getLikedAt());
        return likeDTO;
    }
}
