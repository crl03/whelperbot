package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(int theId);

    void save(User user);

    void deleteById(int theId);

}
