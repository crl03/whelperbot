package com.bighealsinc.whelperbot.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "raid_schedules")
//@IdClass(RaidSchedulesPK.class)
@Data
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
}
