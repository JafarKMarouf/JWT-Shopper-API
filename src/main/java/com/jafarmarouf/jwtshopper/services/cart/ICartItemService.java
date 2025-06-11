package com.jafarmarouf.jwtshopper.services.cart;

import com.jafarmarouf.jwtshopper.dtos.CartItemDto;
import com.jafarmarouf.jwtshopper.models.CartItem;

public interface ICartItemService {
    CartItem addItemToCart(Long productId, Long cartId, int quantity);

    void removeItemFromCart(Long productId, Long cartId);

    void updateItemQuantity(Long productId, Long cartId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);

    CartItemDto convertToCartItemDto(CartItem cartItem);
}
