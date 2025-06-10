package com.jafarmarouf.jwtshopper.services.order;

import com.jafarmarouf.jwtshopper.models.Order;
import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    Order getOrderById(Long orderId);
    List<Order> getUserOrders(Long userId);
}
