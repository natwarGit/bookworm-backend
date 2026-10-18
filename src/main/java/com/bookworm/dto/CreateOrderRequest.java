package com.bookworm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrderRequest(
    @NotNull(message = "Shipping address is required")
    UUID shippingAddressId,

    @NotBlank(message = "Payment method is required")
    String paymentMethod,   // CREDIT_CARD | DEBIT_CARD | UPI | WALLET

    Integer redeemGiftPoints
) {}
