package com.bookworm.dto;

import java.util.UUID;

public record LoginResponse(
    UUID userId,
    String email,
    String fullName,
    String role,
    Integer giftPointsBalance,
    String message
) {}
