package com.ipmcpmjournal.journal.ipmcpm.repository;

import com.ipmcpmjournal.journal.ipmcpm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
