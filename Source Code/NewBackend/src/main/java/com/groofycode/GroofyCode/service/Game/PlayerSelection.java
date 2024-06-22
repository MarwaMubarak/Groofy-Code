package com.groofycode.GroofyCode.service.Game;

import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerSelection {
    private final List<UserModel> waitingPlayers = new ArrayList<>();

    public synchronized void addPlayer(UserModel user) {
        if (!waitingPlayers.contains(user)) {
            waitingPlayers.add(user);
        }
    }

    public synchronized UserModel findFirstPlayerAndRemove(Long userId) {
        for (UserModel user : waitingPlayers) {
            if (!user.getId().equals(userId)) {
                waitingPlayers.remove(user);
                return user;
            }
        }
        return null;
    }

    public synchronized void removePlayer(UserModel user) {
        waitingPlayers.remove(user);
    }
}
