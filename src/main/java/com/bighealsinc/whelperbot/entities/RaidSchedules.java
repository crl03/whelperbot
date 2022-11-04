package com.bighealsinc.whelperbot.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "raid_schedules")
//@IdClass(RaidSchedulesPK.class)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor
public class RaidSchedules {

//    @Id
//    @Column(name = "user_id", nullable = false)
//    private int userId;
//
//    @Id
//    @Column(name = "guild_id", nullable = false)
//    private int guildId;

//    @Id
//    @Column(name = "raid_datetime", nullable = false)
//    private LocalDateTime raidDateTime;

    @EmbeddedId
    private RaidSchedulesPK raidSchedulesPK;

    @Column(name = "game", nullable = false)
    private String game;


    // default property of nullable is true
    @Column(name = "game_server")
    private String gameServer;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.MERGE, CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RaidSchedules that = (RaidSchedules) o;
        return raidSchedulesPK != null && Objects.equals(raidSchedulesPK, that.raidSchedulesPK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raidSchedulesPK);
    }
}
