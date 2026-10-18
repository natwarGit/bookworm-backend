package com.bookworm.service;

import com.bookworm.dto.PaymentRequest;
import com.bookworm.dto.PaymentResponse;
import com.bookworm.entity.Order;
import com.bookworm.entity.Payment;
import com.bookworm.exception.BusinessException;
import com.bookworm.exception.ResourceNotFoundException;
import com.bookworm.repository.OrderRepository;
import com.bookworm.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + request.orderId()));

        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new BusinessException("Cannot process payment for a cancelled order");
        }

        // Prevent duplicate payments
        paymentRepository.findByOrderId(order.getId()).ifPresent(existing -> {
            if (existing.getStatus() == Payment.PaymentStatus.SUCCESS) {
                throw new BusinessException("Payment already completed for order: " + order.getOrderNumber());
            }
        });

        Payment.PaymentMethod method;
        try {
            method = Payment.PaymentMethod.valueOf(request.paymentMethod().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid payment method. Allowed: CREDIT_CARD, DEBIT_CARD, UPI, WALLET");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(method);
        payment.setAmount(order.getFinalAmount());
        // Simulate successful gateway transaction — generate a unique transaction ID
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
        payment.setStatus(Payment.PaymentStatus.SUCCESS);

        Payment saved = paymentRepository.save(payment);

        // Advance order status from PLACED → PROCESSING after payment
        order.setStatus(Order.OrderStatus.PROCESSING);
        orderRepository.save(order);

        return new PaymentResponse(
                saved.getId(),
                order.getId(),
                order.getOrderNumber(),
                saved.getAmount(),
                saved.getPaymentMethod().name(),
                saved.getStatus().name(),
                saved.getTransactionId(),
                saved.getPaymentDate(),
                "Payment successful! Your purchase is confirmed. 🎉"
        );
    }

    public PaymentResponse getPaymentByOrderId(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for order: " + orderId));
        Order order = payment.getOrder();
        return new PaymentResponse(
                payment.getId(),
                order.getId(),
                order.getOrderNumber(),
                payment.getAmount(),
                payment.getPaymentMethod().name(),
                payment.getStatus().name(),
                payment.getTransactionId(),
                payment.getPaymentDate(),
                "Payment record retrieved"
        );
    }
}
