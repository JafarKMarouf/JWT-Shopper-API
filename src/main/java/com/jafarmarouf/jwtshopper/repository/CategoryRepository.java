package com.jafarmarouf.jwtshopper.repository;

import com.jafarmarouf.jwtshopper.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}
