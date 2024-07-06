package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);

    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.solvedProblems WHERE u.id=:id")
    UserModel fetchById(@Param("id") Long id);

    UserModel findByEmailOrUsername(String email, String username);
    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.clanMember WHERE u.email=:data OR u.username=:data")
    UserModel fetchByEmailOrUsernameWithClanMember(String data);

    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.clanMember WHERE u.username=:username")
    UserModel fetchUserWithClanMemberByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.clanMember WHERE u.id=:id")
    UserModel fetchUserWithClanMemberById(@Param("id") Long id);

    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.clanRequest WHERE u.username=:username")
    UserModel fetchUserWithClanRequestByUsername(@Param("username") String username);

    List<UserModel> findByUsernameStartingWithIgnoreCase(String prefix);
    Page<UserModel> findByUsernameStartingWithIgnoreCase(String prefix, Pageable pageable);

    @Query(value = "SELECT * FROM (SELECT id, user_rating, RANK() OVER (ORDER BY user_rating DESC) AS Rank_no FROM users) ranked WHERE id = :userId", nativeQuery = true)
    Integer getUserRank(@Param("userId") Long userId);

    @Query("SELECT u FROM UserModel u ORDER BY u.user_rating DESC")
    Page<UserModel> findLeaderboardOrderByUserRatingDesc(Pageable pageable);

    @Query("SELECT COUNT(u) FROM UserModel u")
    Integer countAllUsers();
}
