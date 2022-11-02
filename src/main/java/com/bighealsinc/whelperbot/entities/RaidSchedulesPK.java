package com.bighealsinc.whelperbot.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class RaidSchedulesPK implements Serializable {

    @Column(name = "user_id")
    private int userId;

    @Column(name = "guild_id")
    private int guildId;

    @Column(name = "raid_datetime")
    private LocalDateTime raidDateTime;

    public RaidSchedulesPK() {

    }

    public RaidSchedulesPK(int userId, int guildId, LocalDateTime raidDateTime) {
        this.userId = userId;
        this.guildId = guildId;
        this.raidDateTime = raidDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RaidSchedulesPK that)) return false;
        return userId == that.userId && guildId == that.guildId && raidDateTime.equals(that.raidDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, guildId, raidDateTime);
    }
}
