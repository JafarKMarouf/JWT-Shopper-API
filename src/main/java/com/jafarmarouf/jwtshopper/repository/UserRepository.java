package com.jafarmarouf.jwtshopper.repository;

import com.jafarmarouf.jwtshopper.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * @param email String
     * @return boolean
     */
    boolean existsUserByEmail(String email);
}
