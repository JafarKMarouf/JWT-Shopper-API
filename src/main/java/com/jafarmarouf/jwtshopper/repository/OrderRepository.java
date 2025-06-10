package com.jafarmarouf.jwtshopper.repository;
import com.jafarmarouf.jwtshopper.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
