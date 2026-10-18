package com.bookworm.controller;

import com.bookworm.dto.CreateOrderRequest;
import com.bookworm.dto.OrderResponse;
import com.bookworm.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders & Checkout", description = "Order placement and 48-hour cancellation management")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private static final UUID DEMO_USER_ID = UUID.fromString("44444444-4444-4444-4444-444444444401");

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Commit checkout, apply gift points, and create order")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestParam(required = false) UUID userId
    ) {
        UUID effectiveUser = userId != null ? userId : DEMO_USER_ID;
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(effectiveUser, request));
    }

    @GetMapping
    @Operation(summary = "Get user order history with Buy It Again support")
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @RequestParam(required = false) UUID userId
    ) {
        UUID effectiveUser = userId != null ? userId : DEMO_USER_ID;
        return ResponseEntity.ok(orderService.getUserOrders(effectiveUser));
    }

    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order within 48-hour SLA window")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable UUID orderId,
            @RequestParam(required = false) UUID userId
    ) {
        UUID effectiveUser = userId != null ? userId : DEMO_USER_ID;
        return ResponseEntity.ok(orderService.cancelOrder(orderId, effectiveUser));
    }
}
