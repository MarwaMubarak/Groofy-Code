package com.groofycode.GroofyCode.model.NewMatch;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OFFICIAL")
public class OfficialMatch extends MatchModel {

    int score1;
    int score2;

    public OfficialMatch() {
    }

    public OfficialMatch(UserModel user1, UserModel user2, int score1, int score2) {
        super(user1, user2);
        this.score1 = score1;
        this.score2 = score2;
    }

    @Override
    public String getMatchType() {
        return "OFFICIAL";
    }
}
