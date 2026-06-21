package com.shop.repository;

import com.shop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Integer> {

    List<Order> findByClientId(int clientId);

    @Query("SELECT o FROM Order o WHERE " +
           "o.client.owner.id = :ownerId " +
           "ORDER BY o.createdAt DESC")
    List<Order> findAllByOwnerId(@Param("ownerId") int ownerId);

    @Query("SELECT COALESCE(SUM(o.amount), 0) " +
           "FROM Order o WHERE " +
           "o.client.owner.id = :ownerId " +
           "AND o.status = 'paid'")
    BigDecimal sumRevenueByOwnerId(@Param("ownerId") int ownerId);

    @Query("SELECT COUNT(o) FROM Order o WHERE " +
           "o.client.owner.id = :ownerId " +
           "AND o.status = 'paid'")
    long countPaidByOwnerId(@Param("ownerId") int ownerId);
}