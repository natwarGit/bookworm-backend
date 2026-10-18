package com.bookworm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AddressRequest(
    @NotNull UUID userId,
    @NotBlank String label,
    @NotBlank String fullName,
    @NotBlank String addressLine1,
    String addressLine2,
    @NotBlank String city,
    @NotBlank String state,
    @NotBlank String pincode,
    @NotBlank String phone
) {}
