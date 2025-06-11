package com.jafarmarouf.jwtshopper.dtos;

import com.jafarmarouf.jwtshopper.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItemDto> Items;
}
