package com.bookworm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
    UUID orderId,
    String orderNumber,
    BigDecimal totalAmount,
    BigDecimal discountAmount,
    BigDecimal finalAmount,
    String status,
    LocalDateTime orderDate,
    LocalDateTime cancellationExpiryDate,
    boolean cancellable
) {}
