package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);

    UserModel findByEmailOrUsername(String email, String username);

    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.clanMember WHERE u.username=:username")
    UserModel fetchUserWithClanMemberByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.clanRequest WHERE u.username=:username")
    UserModel fetchUserWithClanRequestByUsername(@Param("username") String username);

    List<UserModel> findByUsernameStartingWithIgnoreCase(String prefix);
}
