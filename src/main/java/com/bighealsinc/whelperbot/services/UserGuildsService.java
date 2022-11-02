package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.Guild;
import com.bighealsinc.whelperbot.entities.User;

import java.util.List;

public interface UserGuildsService {
    List<User> findUsersByGuildId(int guildId);

    List<Guild> findGuildsByUserId(int userId);
}
