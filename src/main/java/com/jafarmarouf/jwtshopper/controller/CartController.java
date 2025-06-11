package com.jafarmarouf.jwtshopper.controller;

import com.jafarmarouf.jwtshopper.dtos.CartDto;
import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.Cart;
import com.jafarmarouf.jwtshopper.response.ApiResponse;
import com.jafarmarouf.jwtshopper.services.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/{id}/cart")
    public ResponseEntity<ApiResponse> findCartById(@PathVariable Long id) {
        try {
            Cart cart = cartService.getCart(id);
            CartDto cartDto = cartService.convertToCartDto(cart);
            return ResponseEntity.ok(new ApiResponse("Success", cartDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/user-cart")
    public ResponseEntity<ApiResponse> findCartByUserId(@RequestParam Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart is not found!", null));
        }
        CartDto cartDto = cartService.convertToCartDto(cart);
        return ResponseEntity.ok(new ApiResponse("Success", cartDto));
    }

    @DeleteMapping("/{id}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id) {
        try {
            cartService.clearCartById(id);
            return ResponseEntity.ok(new ApiResponse("Cart Cleared Success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("{id}/cart/total-amount")
    public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long id) {
        try {
            BigDecimal totalAmount = cartService.getTotalPrice(id);
            return ResponseEntity.ok(new ApiResponse("Total Amount", totalAmount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}

