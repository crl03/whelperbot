package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.UserGuilds;

public interface UserGuildsService {

    UserGuilds findByCompositeId(int userId, int guildId);

    void save(UserGuilds userGuilds);
}
