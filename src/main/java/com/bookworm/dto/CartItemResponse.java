package com.bookworm.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(
    UUID id,
    UUID bookId,
    String title,
    String format,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal subtotal
) {}
