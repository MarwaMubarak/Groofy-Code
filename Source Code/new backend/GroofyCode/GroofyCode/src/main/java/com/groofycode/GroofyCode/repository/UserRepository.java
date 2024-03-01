package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel,Long> {
}
