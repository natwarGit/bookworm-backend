package com.bookworm.service;

import com.bookworm.dto.CreateOrderRequest;
import com.bookworm.dto.OrderResponse;
import com.bookworm.entity.*;
import com.bookworm.exception.BusinessException;
import com.bookworm.exception.ResourceNotFoundException;
import com.bookworm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        UserRepository userRepository,
                        BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public OrderResponse createOrder(UUID userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("Active cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new BusinessException("Cannot checkout with an empty cart");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            Book book = item.getBook();
            if (book.getStockQuantity() < item.getQuantity()) {
                throw new BusinessException("Insufficient stock for book: " + book.getTitle());
            }
            book.setStockQuantity(book.getStockQuantity() - item.getQuantity());
            bookRepository.save(book);
            total = total.add(book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // Apply gift points redemption (1 point = ₹1)
        int pointsToRedeem = 0;
        BigDecimal discount = BigDecimal.ZERO;
        if (request.redeemGiftPoints() != null && request.redeemGiftPoints() > 0) {
            pointsToRedeem = Math.min(request.redeemGiftPoints(), user.getGiftPointsBalance());
            discount = BigDecimal.valueOf(pointsToRedeem);
            user.setGiftPointsBalance(user.getGiftPointsBalance() - pointsToRedeem);
        }

        BigDecimal finalAmount = total.subtract(discount).max(BigDecimal.ZERO);

        Order order = new Order();
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setUser(user);
        order.setTotalAmount(total);
        order.setDiscountAmount(discount);
        order.setFinalAmount(finalAmount);
        order.setGiftPointsRedeemed(pointsToRedeem);
        order.setStatus(Order.OrderStatus.PLACED);

        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setBook(ci.getBook());
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtPurchase(ci.getBook().getPrice());
            order.getItems().add(oi);
        }

        Order saved = orderRepository.save(order);

        // Award 5% points for the purchase
        user.setGiftPointsBalance(user.getGiftPointsBalance() + finalAmount.multiply(new BigDecimal("0.05")).intValue());
        userRepository.save(user);

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return mapToResponse(saved);
    }

    @Transactional
    public OrderResponse cancelOrder(UUID orderId, UUID userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessException("Unauthorized order cancellation attempt");
        }

        if (!order.isEligibleForCancellation()) {
            throw new BusinessException("Order cannot be cancelled. The 48-hour cancellation window has expired.");
        }

        for (OrderItem item : order.getItems()) {
            Book book = item.getBook();
            book.setStockQuantity(book.getStockQuantity() + item.getQuantity());
            bookRepository.save(book);
        }

        if (order.getGiftPointsRedeemed() > 0) {
            User user = order.getUser();
            user.setGiftPointsBalance(user.getGiftPointsBalance() + order.getGiftPointsRedeemed());
            userRepository.save(user);
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        Order updated = orderRepository.save(order);

        return mapToResponse(updated);
    }

    public List<OrderResponse> getUserOrders(UUID userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getTotalAmount(),
                order.getDiscountAmount(),
                order.getFinalAmount(),
                order.getStatus().name(),
                order.getOrderDate(),
                order.getCancellationExpiryDate(),
                order.isEligibleForCancellation()
        );
    }
}
