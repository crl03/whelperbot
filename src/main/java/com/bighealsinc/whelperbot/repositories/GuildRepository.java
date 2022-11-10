package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.Guild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuildRepository extends JpaRepository<Guild, Integer> {

    Optional<Guild> findByGuildId(long guildId);
}