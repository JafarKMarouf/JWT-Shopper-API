package com.jafarmarouf.jwtshopper.services.order;

import com.jafarmarouf.jwtshopper.dtos.OrderDto;
import com.jafarmarouf.jwtshopper.models.Order;
import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrderById(Long orderId);
    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertOrdersToDto(Order order);
}
