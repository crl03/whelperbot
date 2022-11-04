package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    User findById(int theId);

    Optional<User> findByDiscordId(long discordId);

    void save(User user);

    void deleteById(int theId);

}
