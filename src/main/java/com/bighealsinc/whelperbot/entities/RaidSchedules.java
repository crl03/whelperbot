package com.bighealsinc.whelperbot.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "raid_schedules")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor
public class RaidSchedules {

    @Id
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "guild_id", nullable = false)
    private int guildId;

    @Column(name = "game", nullable = false)
    private String game;

    @Column(name = "raid_datetime", nullable = false)
    private LocalDateTime raidDateTime;

    // default property of nullable is true
    @Column(name = "game_server")
    private String gameServer;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne
    @JsonBackReference
    private User user;
}
