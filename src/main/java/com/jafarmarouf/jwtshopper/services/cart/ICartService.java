package com.jafarmarouf.jwtshopper.services.cart;

import com.jafarmarouf.jwtshopper.dtos.CartDto;
import com.jafarmarouf.jwtshopper.models.Cart;
import com.jafarmarouf.jwtshopper.models.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCartById(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart getCartByUserId(Long userId);

    Cart initializeNewCart(User user);

    CartDto convertToCartDto(Cart cart);
}
