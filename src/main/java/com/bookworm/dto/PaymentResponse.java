package com.bookworm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
    UUID paymentId,
    UUID orderId,
    String orderNumber,
    BigDecimal amount,
    String paymentMethod,
    String status,
    String transactionId,
    LocalDateTime paymentDate,
    String message
) {}
