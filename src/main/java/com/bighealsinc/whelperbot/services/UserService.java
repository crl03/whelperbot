package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(int theId);

    User findByDiscordId(long discordId);

    void save(User user);

    void deleteById(int theId);

}
