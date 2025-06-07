package com.jafarmarouf.jwtshopper.services.cart;

import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.Cart;
import com.jafarmarouf.jwtshopper.models.CartItem;
import com.jafarmarouf.jwtshopper.models.Product;
import com.jafarmarouf.jwtshopper.repository.CartItemRepository;
import com.jafarmarouf.jwtshopper.repository.CartRepository;
import com.jafarmarouf.jwtshopper.services.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final ICartService cartService;
    private final IProductService productService;
    private final CartRepository cartRepository;

    /**
     * @param productId Long
     * @param cartId    Long
     * @param quantity  int
     */
    @Override
    public void addItemToCart(Long productId, Long cartId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);

        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    /**
     * @param productId Long
     * @param cartId    Long
     */
    @Override
    public void removeItemFromCart(Long productId, Long cartId) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = getCartItem(productId, cartId);
        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }

    /**
     * @param productId Long
     * @param cartId    Long
     * @param quantity  int
     */
    @Override
    public void updateItemQuantity(Long productId, Long cartId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(productService.getProductById(productId).getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    /**
     * @param productId Long
     * @param cartId    Long
     * @return CartItem
     */
    @Override
    public CartItem getCartItem(Long productId, Long cartId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}
