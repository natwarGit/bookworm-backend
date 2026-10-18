package com.bookworm.dto;

import java.util.UUID;

public record AddressResponse(
    UUID id,
    String label,
    String fullName,
    String addressLine1,
    String addressLine2,
    String city,
    String state,
    String pincode,
    String phone
) {}
