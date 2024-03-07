package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.TokenInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenInfoModel,Long> {
    Optional<TokenInfoModel> findByRefreshToken (String refreshToken);

}
