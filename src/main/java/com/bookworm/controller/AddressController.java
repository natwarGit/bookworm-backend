package com.bookworm.controller;

import com.bookworm.dto.AddressRequest;
import com.bookworm.dto.AddressResponse;
import com.bookworm.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@Tag(name = "Addresses", description = "Manage delivery addresses — select at checkout")
@CrossOrigin(origins = "*")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    @Operation(summary = "Get all delivery addresses for a user")
    public ResponseEntity<List<AddressResponse>> getUserAddresses(@RequestParam UUID userId) {
        return ResponseEntity.ok(addressService.getAddressesForUser(userId));
    }

    @PostMapping
    @Operation(summary = "Add a new delivery address")
    public ResponseEntity<AddressResponse> addAddress(@Valid @RequestBody AddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddress(request));
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Delete a delivery address")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
