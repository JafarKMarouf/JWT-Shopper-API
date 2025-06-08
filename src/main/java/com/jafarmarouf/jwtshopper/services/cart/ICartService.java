package com.jafarmarouf.jwtshopper.services.cart;

import com.jafarmarouf.jwtshopper.models.Cart;
import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void removeCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();
}
