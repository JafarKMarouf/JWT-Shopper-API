package com.jafarmarouf.jwtshopper.repository;

import com.jafarmarouf.jwtshopper.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
