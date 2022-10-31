package com.bighealsinc.whelperbot.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import discord4j.core.object.entity.Guild;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_guilds")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGuilds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guild_id")
    private int guildId;

    @ManyToOne
    @JsonBackReference
    private User user;

    @ManyToOne
    @JsonBackReference
    private Guild guild;
}
