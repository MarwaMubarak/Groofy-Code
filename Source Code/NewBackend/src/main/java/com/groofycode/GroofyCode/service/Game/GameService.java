package com.groofycode.GroofyCode.service.Game;


import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.repository.Game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    // Additional service methods if needed
}