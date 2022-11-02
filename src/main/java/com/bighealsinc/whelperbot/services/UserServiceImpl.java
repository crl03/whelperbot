package com.bighealsinc.whelperbot.services;

import com.bighealsinc.whelperbot.entities.User;
import com.bighealsinc.whelperbot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public  UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public void save(User user) {
        userRepository.save(user);

    }

    @Override
    public void deleteById(int theId) {
        userRepository.deleteById(theId);
    }
}
