package com.bighealsinc.whelperbot.repositories;

import com.bighealsinc.whelperbot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}