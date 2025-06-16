package com.jafarmarouf.jwtshopper.repository;

import com.jafarmarouf.jwtshopper.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);
}
