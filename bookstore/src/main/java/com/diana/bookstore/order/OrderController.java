package com.diana.bookstore.order;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public void createdOrder(Authentication authentication) {
        orderService.createdOrder(authentication.getName());
    }

    @GetMapping
    public List<OrderResponseDto> getOrders(Authentication authentication) {
        return orderService.getOrders(authentication.getName());
    }
}
