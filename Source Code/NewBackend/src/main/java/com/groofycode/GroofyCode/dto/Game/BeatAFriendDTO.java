package com.groofycode.GroofyCode.dto.Game;


import com.groofycode.GroofyCode.model.Game.BeatAFriend;
import com.groofycode.GroofyCode.model.User.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class BeatAFriendDTO extends GameDTO {

    public BeatAFriendDTO(BeatAFriend beatAFriend, Object problemStatement) {
        super(
                beatAFriend.getId(),
                beatAFriend.getPlayers1().stream().map(UserModel::getId).collect(Collectors.toList()),
                beatAFriend.getPlayers2().stream().map(UserModel::getId).collect(Collectors.toList()),
                beatAFriend.getProblemUrl(),
                beatAFriend.getStartTime(),
                beatAFriend.getEndTime(),
                beatAFriend.getDuration(),
                "Friendly",
                beatAFriend.getGameStatus().toString(),
                null // assign it later
        );
        setProblemStatement(problemStatement);
    }
}
