package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.Guild;
import com.bighealsinc.whelperbot.entities.User;
import com.bighealsinc.whelperbot.entities.UserGuilds;
import com.bighealsinc.whelperbot.entities.UserGuildsPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGuildsRepository extends JpaRepository<UserGuilds, UserGuildsPK> {

}