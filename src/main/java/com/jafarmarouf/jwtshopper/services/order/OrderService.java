package com.jafarmarouf.jwtshopper.services.order;

import com.jafarmarouf.jwtshopper.dtos.OrderDto;
import com.jafarmarouf.jwtshopper.enums.OrderStatus;
import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.Cart;
import com.jafarmarouf.jwtshopper.models.Order;
import com.jafarmarouf.jwtshopper.models.OrderItem;
import com.jafarmarouf.jwtshopper.models.Product;
import com.jafarmarouf.jwtshopper.repository.OrderRepository;
import com.jafarmarouf.jwtshopper.repository.ProductRepository;
import com.jafarmarouf.jwtshopper.repository.UserRepository;
import com.jafarmarouf.jwtshopper.services.cart.ICartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    /**
     * @param userId Long
     * @return Order
     */
    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCartById(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems()
                .stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    if (product.getInventory() < cartItem.getQuantity()) {
                        throw new ResourceNotFoundException("Not found enough from product " + product.getName());
                    }
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            product,
                            order,
                            cartItem.getUnitPrice(),
                            cartItem.getQuantity()
                    );
                }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem ->
                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * @param orderId Long
     * @return Order
     */
    @Override
    public OrderDto getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertOrdersToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    /**
     * @param userId Long
     * @return List<Order>
     */
    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertOrdersToDto).toList();
    }

    @Override
    public OrderDto convertOrdersToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
