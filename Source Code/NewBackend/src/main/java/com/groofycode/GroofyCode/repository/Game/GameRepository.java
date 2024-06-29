package com.groofycode.GroofyCode.repository.Game;

import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.submissions WHERE g.id = ?1")
    Game fetchById(Long id);

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.players1 p1 LEFT JOIN FETCH g.players2 p2 WHERE p1 = ?1 OR p2 = ?1")
    List<Game> findAllGamesByUser(UserModel user);

    @Query("SELECT g FROM Game g JOIN g.players1 p WHERE p = :user " +
            "UNION " +
            "SELECT g FROM Game g JOIN g.players2 p WHERE p = :user")
    List<Game> findGamesByUser(@Param("user") UserModel user);
}