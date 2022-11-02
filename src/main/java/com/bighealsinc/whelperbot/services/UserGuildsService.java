package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.Guild;
import com.bighealsinc.whelperbot.entities.User;
import com.bighealsinc.whelperbot.entities.UserGuilds;

import java.util.List;

public interface UserGuildsService {

    UserGuilds findUserGuildByCompositeId(int userId, int guildId);

    void save(UserGuilds userGuilds);
}
