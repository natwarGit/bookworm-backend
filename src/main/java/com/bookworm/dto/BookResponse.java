package com.bookworm.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BookResponse(
    UUID id,
    String isbn,
    String title,
    String authorName,
    String categoryName,
    String format,
    String language,
    BigDecimal price,
    Integer stockQuantity,
    String coverImageUrl,
    Boolean isBestseller,
    Boolean isNewLaunch,
    String tentativeDeliveryDate
) {}
