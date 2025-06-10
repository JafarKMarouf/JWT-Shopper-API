package com.jafarmarouf.jwtshopper.services.cart;

import com.jafarmarouf.jwtshopper.models.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCartById(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart getCartByUserId(Long userId);

    Long initializeNewCart();
}
