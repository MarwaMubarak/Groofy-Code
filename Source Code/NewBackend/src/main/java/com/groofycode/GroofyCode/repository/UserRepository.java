package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Long> {
    UserModel findByUsername(String username);
    UserModel findByEmailOrUsername(String email, String username);
    List<UserModel> findByUsernameStartingWithIgnoreCase(String prefix);
}
