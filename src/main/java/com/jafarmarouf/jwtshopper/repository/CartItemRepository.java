package com.jafarmarouf.jwtshopper.repository;

import com.jafarmarouf.jwtshopper.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long cartId);
}
