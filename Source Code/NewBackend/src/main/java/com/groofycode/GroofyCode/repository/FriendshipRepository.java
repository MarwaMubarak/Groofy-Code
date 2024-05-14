package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.FriendshipModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipModel, Long> {

    @Query("SELECT f FROM FriendshipModel f WHERE (f.senderId = :userId OR f.receiverId = :userId)")
    Page<FriendshipModel> getAllPage(Long userId, Pageable pageable);

    @Query("SELECT f FROM FriendshipModel f WHERE (f.senderId = :userId OR f.receiverId = :userId) AND f.status = 'pending'")
    Page<FriendshipModel> getPendingPage(Long userId, Pageable pageable);

    @Query("SELECT f FROM FriendshipModel f WHERE (f.senderId = :userId OR f.receiverId = :userId) AND f.status = 'accepted'")
    Page<FriendshipModel> getAcceptedPage(Long userId, Pageable pageable);

    @Query("SELECT COUNT(f) FROM FriendshipModel f WHERE (f.senderId = :userId OR f.receiverId = :userId)")
    int getAllCount(Long userId);

    @Query("SELECT COUNT(f) FROM FriendshipModel f WHERE (f.senderId = :userId OR f.receiverId = :userId) AND f.status = 'pending'")
    int getPendingCount(Long userId);

    @Query("SELECT COUNT(f) FROM FriendshipModel f WHERE (f.senderId = :userId OR f.receiverId = :userId) AND f.status = 'accepted'")
    int getAcceptedCount(Long userId);

    @Query("SELECT f FROM FriendshipModel f WHERE ((f.senderId = :userId1 AND f.receiverId = :userId2) OR (f.senderId = :userId2 AND f.receiverId = :userId1)) AND f.status = 'accepted'")
    Optional<FriendshipModel> checkAcceptedRequest(Long userId1, Long userId2);

    @Query("SELECT f FROM FriendshipModel f WHERE (f.senderId = :senderId AND f.receiverId = :receiverId) AND f.status = 'pending'")
    Optional<FriendshipModel> checkPendingRequest(Long senderId, Long receiverId);

    @Query("SELECT f from FriendshipModel f WHERE (f.senderId = :senderId AND f.receiverId = :receiverId) OR (f.senderId = :receiverId AND f.receiverId = :senderId)")
    FriendshipModel getUserFriendshipStatus(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

}

