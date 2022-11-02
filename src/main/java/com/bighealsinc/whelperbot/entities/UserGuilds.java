package com.bighealsinc.whelperbot.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_guilds")
@IdClass(UserGuildsPK.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGuilds implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guild_id")
    private int guildId;

    @Column(name = "messages")
    private int messages;

    @Column(name = "reactions")
    private int reactions;

    @Column(name = "scheduled_raids")
    private int scheduledRaids;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.MERGE, CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.MERGE, CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "guild_id", insertable = false, updatable = false)
    @JsonBackReference
    private Guild guild;
}
