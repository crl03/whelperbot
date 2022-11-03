package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(
            value = "SELECT id FROM users u WHERE u.user_id = :discordId",
            nativeQuery = true
    )
    User findByDiscordId(long discordId);
}