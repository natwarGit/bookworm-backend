package com.bookworm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PaymentRequest(
    @NotNull(message = "Order ID is required")
    UUID orderId,

    @NotBlank(message = "Payment method is required")
    String paymentMethod   // CREDIT_CARD | DEBIT_CARD | UPI | WALLET
) {}
