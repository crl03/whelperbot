package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.Guild;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuildRepository extends JpaRepository<Guild, Integer> {
}