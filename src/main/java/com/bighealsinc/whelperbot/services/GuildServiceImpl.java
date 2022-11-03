package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.Guild;
import com.bighealsinc.whelperbot.repositories.GuildRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class GuildServiceImpl implements GuildService {

    private GuildRepository guildRepository;

    @Autowired
    public GuildServiceImpl(GuildRepository guildRepository) {
        this.guildRepository = guildRepository;
    }

    @Override
    public List<Guild> findAll() {
        return guildRepository.findAll();
    }

    @Override
    public Guild findById(int theId) {
        Optional<Guild> result = guildRepository.findById(theId);

        Guild foundGuild;
        if (result.isPresent()) {
            foundGuild = result.get();
        } else {
            throw new RuntimeException("Could not find guild Id: " + theId);
        }
        return foundGuild;
    }

    @Override
    public Guild findByDiscordGuildId(long discordId) {
        Guild result = guildRepository.findByDiscordGuildId(discordId);

        // if result is null save a new guild into the database
        return result;
    }

    @Override
    public void save(Guild guild) {
        guildRepository.save(guild);
    }

    @Override
    public void deleteById(int theId) {
        guildRepository.deleteById(theId);
    }
}
