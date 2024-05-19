package com.groofycode.GroofyCode.model.NewMatch;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

@Entity
@DiscriminatorValue("CASUAL")
public class CasualMatch extends MatchModel {

    public CasualMatch() {}

    public CasualMatch(UserModel user1, UserModel user2) {
        super(user1, user2);
    }

    @Override
    public String getMatchType() {
        return "CASUAL";
    }
}
