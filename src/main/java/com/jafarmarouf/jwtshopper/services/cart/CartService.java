package com.jafarmarouf.jwtshopper.services.cart;

import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.Cart;
import com.jafarmarouf.jwtshopper.repository.CartItemRepository;
import com.jafarmarouf.jwtshopper.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * @param id Long
     * @return Cart
     */
    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    /**
     * @param id Long
     */
    @Transactional
    @Override
    public void clearCartById(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
    }

    /**
     * @param id Long
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    /**
     * @param userId Long
     * @return Cart
     */
    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findCartByUserId(userId);
    }

    /**
     * @return Long
     */
    @Override
    public Long initializeNewCart() {
        Cart newCart = new Cart();
        return cartRepository.save(newCart).getId();
    }
}
