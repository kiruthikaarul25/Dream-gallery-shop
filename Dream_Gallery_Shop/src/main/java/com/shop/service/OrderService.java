package com.shop.service;

import com.shop.model.Order;
import com.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // ─── Order Create ───────────────────────────────
    public Order createOrder(Order order) {
        order.setStatus("pending");
        order.setCreatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // ─── Client Orders ──────────────────────────────
    public List<Order> getOrdersByClient(int clientId) {
        return orderRepository.findByClientId(clientId);
    }

    // ─── Owner Orders ───────────────────────────────
    public List<Order> getAllOrdersByOwner(int ownerId) {
        return orderRepository.findAllByOwnerId(ownerId);
    }

    // ─── Find by Owner ──────────────────────────────
    public List<Order> findByOwner(int ownerId) {
        return orderRepository.findAllByOwnerId(ownerId);
    }

    // ─── Mark as Paid ───────────────────────────────
    public Order markAsPaid(int orderId, String paymentMode) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus("paid");
        order.setPaymentMode(paymentMode);
        order.setPaidAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // ─── Cancel ─────────────────────────────────────
    public Order cancelOrder(int orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus("cancelled");
        return orderRepository.save(order);
    }

    // ─── Total Revenue ──────────────────────────────
    public BigDecimal getTotalRevenue(int ownerId) {
        BigDecimal revenue = orderRepository.sumRevenueByOwnerId(ownerId);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    // ─── Paid Orders Count ──────────────────────────
    public long countPaidOrders(int ownerId) {
        return orderRepository.countPaidByOwnerId(ownerId);
    }

    // ─── Get by ID ──────────────────────────────────
    public Optional<Order> getById(int id) {
        return orderRepository.findById(id);
    }
}