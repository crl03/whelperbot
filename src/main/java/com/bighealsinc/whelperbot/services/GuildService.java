package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.Guild;

import java.util.List;
import java.util.Optional;

public interface GuildService {

    List<Guild> findAll();

    Guild findById(int theId);

    Optional<Guild> findByDiscordGuildId(long discordId);

    void save(Guild guild);

    void deleteById(int theId);

}
