package com.jafarmarouf.jwtshopper.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDto {
    private Long userId;
    private List<CartItemDto> items;
    private BigDecimal totalAmount;
}
