package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.UserGuilds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGuildsRepository extends JpaRepository<UserGuilds, Integer> {
}