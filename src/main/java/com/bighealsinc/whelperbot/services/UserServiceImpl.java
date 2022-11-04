package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.User;
import com.bighealsinc.whelperbot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public  UserServiceImpl(UserRepository userRepository) {
        userRepository = userRepository;
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int theId) {
        Optional<User> result = userRepository.findById(theId);

        User foundUser;
        if (result.isPresent()) {
            foundUser = result.get();
        } else {
            throw new RuntimeException("Did not find user id: " + theId);
        }
        return foundUser;
    }

    @Override
    public Optional<User> findByDiscordId(long discordId) {
        Optional<User> result = userRepository.findByDiscordId(discordId);

        // if result is null save new user into database

//        Optional<User> result = Optional.ofNullable(userRepository.findByDiscordId(discordId));

//        User foundUser;
//        if (result.isPresent()) {
//            foundUser = result.get();
//        } else {
//            throw new RuntimeException("Did not find user id: " + discordId);
//        }
        return result;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);

    }

    @Override
    public void deleteById(int theId) {
        userRepository.deleteById(theId);
    }
}
