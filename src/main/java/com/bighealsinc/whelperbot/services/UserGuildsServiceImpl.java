package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.Guild;
import com.bighealsinc.whelperbot.entities.User;
import com.bighealsinc.whelperbot.entities.UserGuilds;
import com.bighealsinc.whelperbot.entities.UserGuildsPK;
import com.bighealsinc.whelperbot.repositories.UserGuildsRepository;

import java.util.List;
import java.util.Optional;

public class UserGuildsServiceImpl implements UserGuildsService {

    private UserGuildsRepository userGuildsRepository;

    public UserGuildsServiceImpl (UserGuildsRepository userGuildsRepository) {
        this.userGuildsRepository = userGuildsRepository;
    }

    @Override
    public UserGuilds findUserGuildByCompositeId(int userId, int guildId) {
        Optional<UserGuilds> result = userGuildsRepository.findById(new UserGuildsPK(userId, guildId));

        UserGuilds foundUserGuilds = null;
        if (result.isPresent()) {
            foundUserGuilds = result.get();
        } else {
            throw new RuntimeException("Cannot find UserGuildsPK: " + userId + guildId);
        }
        return foundUserGuilds;
    }

    @Override
    public void save(UserGuilds userGuilds) {
        userGuildsRepository.save(userGuilds);
    }
}
