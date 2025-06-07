package com.jafarmarouf.jwtshopper.controller;

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

    @GetMapping("/{id}/my-carts")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long id) {
        try {
            Cart cart = cartService.getCart(id);
            return ResponseEntity.ok(new ApiResponse("Success", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}/clear")
    public ResponseEntity<ApiResponse> removeCart(@PathVariable Long id) {
        try {
            cartService.removeCart(id);
            return ResponseEntity.ok(new ApiResponse("Cart Cleared Success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("{id}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long id) {
        try {
            BigDecimal totalAmount = cartService.getTotalPrice(id);
            return ResponseEntity.ok(new ApiResponse("Total price", totalAmount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}

