package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.Guild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GuildRepository extends JpaRepository<Guild, Integer> {

    @Query(
            value = "SELECT id FROM guilds g WHERE g.guild_id = :guildId",
            nativeQuery = true
    )
    Guild findByDiscordGuildId(long guildId);
}