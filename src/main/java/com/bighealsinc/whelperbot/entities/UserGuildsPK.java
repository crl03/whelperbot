package com.bighealsinc.whelperbot.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


public class UserGuildsPK implements Serializable{

    @Column(name = "user_id")
    private int userId;

    @Column(name = "guild_id")
    private int guildId;

    public UserGuildsPK() {

    }

    public UserGuildsPK(int userId, int guildId) {
        this.userId = userId;
        this.guildId = guildId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserGuildsPK that)) return false;
        return userId == that.userId && guildId == that.guildId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, guildId);
    }
}
