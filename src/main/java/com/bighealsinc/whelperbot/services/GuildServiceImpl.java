package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.Guild;
import com.bighealsinc.whelperbot.repositories.GuildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuildServiceImpl implements GuildService {

    @Autowired
    private GuildRepository guildRepository;

    @Autowired
    public GuildServiceImpl(GuildRepository guildRepository) {
        guildRepository = guildRepository;
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
    public Optional<Guild> findByDiscordGuildId(long discordId) {
        Optional<Guild> result = guildRepository.findByGuildId(discordId);

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
