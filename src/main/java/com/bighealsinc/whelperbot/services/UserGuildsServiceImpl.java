package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.UserGuilds;
import com.bighealsinc.whelperbot.entities.UserGuildsPK;
import com.bighealsinc.whelperbot.repositories.UserGuildsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserGuildsServiceImpl implements UserGuildsService {

    @Autowired
    private UserGuildsRepository userGuildsRepository;

    @Autowired
    public UserGuildsServiceImpl (UserGuildsRepository userGuildsRepository) {
        userGuildsRepository = userGuildsRepository;
    }

    @Override
    public UserGuilds findByCompositeId(int userId, int guildId) {
        Optional<UserGuilds> result = userGuildsRepository.findById(new UserGuildsPK(userId, guildId));

        UserGuilds foundUserGuilds;
        if (result.isPresent()) {
            foundUserGuilds = result.get();
        } else {
            throw new RuntimeException("Cannot find UserGuildsPK: " + userId + guildId);
        }
        return foundUserGuilds;
    }

    @Override
    public List<UserGuilds> findAllByGuildId(int guildId) {
        List<UserGuilds> userlist = userGuildsRepository.findAllByGuildId(guildId);


        return userlist;
    }

    @Override
    public void save(UserGuilds userGuilds) {
        userGuildsRepository.save(userGuilds);
    }
}
