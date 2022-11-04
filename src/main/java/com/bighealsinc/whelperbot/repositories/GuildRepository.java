package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.Guild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuildRepository extends JpaRepository<Guild, Integer> {

//    @Query(
//            value = "SELECT id FROM guilds g WHERE g.guild_id = :guildId",
//            nativeQuery = true
//    )
    Optional<Guild> findByGuildId(long guildId);
}