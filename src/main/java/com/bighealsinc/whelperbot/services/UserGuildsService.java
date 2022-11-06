package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.UserGuilds;

import java.util.List;

public interface UserGuildsService {

    UserGuilds findByCompositeId(int userId, int guildId);

    List<UserGuilds> findAllByGuildId(int guildId);

    void save(UserGuilds userGuilds);
}
