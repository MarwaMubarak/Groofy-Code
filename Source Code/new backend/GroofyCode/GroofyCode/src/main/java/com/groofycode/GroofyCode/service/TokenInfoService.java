package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.model.TokenInfoModel;
import com.groofycode.GroofyCode.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenInfoService {
    @Autowired
    private  TokenRepository tokenRepository;

    public TokenInfoModel findById(Long id) {

        return tokenRepository.findById(id).orElse(null);
    }

    public TokenInfoModel findByRefreshToken(String refreshToken) {

        Optional<TokenInfoModel> tokenInfo = tokenRepository.findByRefreshToken(refreshToken);
        return (tokenInfo.orElse(null));

        //return tokenRepository.findByRefreshToken(refreshToken);
    }

    public TokenInfoModel save(TokenInfoModel entity) {

        return tokenRepository.save(entity);
    }

    public void deleteById (Long id) {

        tokenRepository.deleteById(id);
    }
}
