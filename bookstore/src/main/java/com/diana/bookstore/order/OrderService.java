package com.diana.bookstore.order;

import com.diana.bookstore.basket.Basket;
import com.diana.bookstore.basket.BasketItemRepository;
import com.diana.bookstore.basket.BasketRepository;
import com.diana.bookstore.user.User;
import com.diana.bookstore.user.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BasketItemRepository basketItemRepository;
    private final BasketRepository basketRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository,BasketItemRepository basketItemRepository, BasketRepository basketRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.basketItemRepository = basketItemRepository;
        this.basketRepository = basketRepository;
    }

    public OrderResponseDto toDto(Order order) {
        List<OrderItemDto> items = order.getItems().stream()
                .map(item -> new OrderItemDto(
                        item.getTitle(),
                        item.getAuthor(),
                        item.getPrice(),
                        item.getQuantity()
                )).toList();

        return new OrderResponseDto(
                items,
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getStatus()
        );
    }

    public void createdOrder(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Not found user with email = " + email));

        Basket basket = basketRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Not found basket"));

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDate.now());
        order.setStatus(OrderStatus.PAID);

        List<OrderItem> items = basket.getItems().stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setTitle(item.getBook().getTitle());
                    orderItem.setAuthor(item.getBook().getAuthor());
                    orderItem.setPrice(item.getBook().getPrice());
                    orderItem.setQuantity(item.getQuantity());
                    return orderItem;
                })
                .toList();
        order.setItems(items);

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        orderRepository.save(order);

        basket.getItems().clear();
        basketRepository.save(basket);
    }

    public List<OrderResponseDto> getOrders(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Not found user with email = " + email));

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(this::toDto)
                .toList();
    }
}
