package com.bookworm.service;

import com.bookworm.dto.AddToCartRequest;
import com.bookworm.dto.CartItemResponse;
import com.bookworm.dto.CartResponse;
import com.bookworm.entity.Book;
import com.bookworm.entity.Cart;
import com.bookworm.entity.CartItem;
import com.bookworm.entity.User;
import com.bookworm.exception.BusinessException;
import com.bookworm.exception.ResourceNotFoundException;
import com.bookworm.repository.BookRepository;
import com.bookworm.repository.CartRepository;
import com.bookworm.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Cart getOrCreateCart(UUID userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
            Cart newCart = new Cart(user);
            return cartRepository.save(newCart);
        });
    }

    @Transactional
    public CartResponse getCartResponse(UUID userId) {
        Cart cart = getOrCreateCart(userId);
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse addToCart(UUID userId, AddToCartRequest request) {
        Cart cart = getOrCreateCart(userId);
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + request.bookId()));

        if (book.getStockQuantity() < request.quantity()) {
            throw new BusinessException("Requested quantity exceeds available stock (" + book.getStockQuantity() + ")");
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.quantity());
        } else {
            CartItem newItem = new CartItem(cart, book, request.quantity());
            cart.getItems().add(newItem);
        }

        Cart saved = cartRepository.save(cart);
        return mapToResponse(saved);
    }

    @Transactional
    public CartResponse clearCart(UUID userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        Cart saved = cartRepository.save(cart);
        return mapToResponse(saved);
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream().map(item -> new CartItemResponse(
                item.getId(),
                item.getBook().getId(),
                item.getBook().getTitle(),
                item.getBook().getFormat().name(),
                item.getBook().getPrice(),
                item.getQuantity(),
                item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        )).collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalCount = items.stream().mapToInt(CartItemResponse::quantity).sum();

        return new CartResponse(cart.getId(), items, total, totalCount);
    }
}
