package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByDiscordId(long discordId);
}