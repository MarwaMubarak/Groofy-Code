package com.groofycode.GroofyCode.model.NewMatch;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SOLO")
public class SoloMatch extends MatchModel {

    public SoloMatch() {}

    public SoloMatch(UserModel user1) {
        super(user1);
    }

    @Override
    public String getMatchType() {
        return "SOLO";
    }
}
