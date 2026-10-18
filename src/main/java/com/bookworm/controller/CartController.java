package com.bookworm.controller;

import com.bookworm.dto.AddToCartRequest;
import com.bookworm.dto.CartResponse;
import com.bookworm.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Shopping Cart", description = "Basket management operations")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;
    // Default demo user ID for testing convenience
    private static final UUID DEMO_USER_ID = UUID.fromString("44444444-4444-4444-4444-444444444401");

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Get user shopping cart")
    public ResponseEntity<CartResponse> getCart(
            @RequestParam(required = false) UUID userId
    ) {
        UUID effectiveUser = userId != null ? userId : DEMO_USER_ID;
        return ResponseEntity.ok(cartService.getCartResponse(effectiveUser));
    }

    @PostMapping
    @Operation(summary = "Add a book to shopping cart")
    public ResponseEntity<CartResponse> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            @RequestParam(required = false) UUID userId
    ) {
        UUID effectiveUser = userId != null ? userId : DEMO_USER_ID;
        return ResponseEntity.ok(cartService.addToCart(effectiveUser, request));
    }

    @DeleteMapping
    @Operation(summary = "Clear shopping cart")
    public ResponseEntity<CartResponse> clearCart(
            @RequestParam(required = false) UUID userId
    ) {
        UUID effectiveUser = userId != null ? userId : DEMO_USER_ID;
        return ResponseEntity.ok(cartService.clearCart(effectiveUser));
    }
}
