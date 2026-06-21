package com.shop.repository;

import com.shop.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface OwnerRepository
        extends JpaRepository<Owner, Integer> {

    // Email மூலம் find
    Optional<Owner> findByEmail(String email);

    // Email exists check
    boolean existsByEmail(String email);

    // Phone மூலம் find
    Optional<Owner> findByPhone(String phone);

    // Studio name search
    List<Owner> findByStudioNameContainingIgnoreCase(
        String studioName);

    // Owner stats
    @Query("SELECT COUNT(c) FROM Client c " +
           "WHERE c.owner.id = :ownerId")
    long countClientsByOwnerId(
        @Param("ownerId") int ownerId);

    @Query("SELECT COUNT(p) FROM Photo p " +
           "WHERE p.owner.id = :ownerId " +
           "AND p.isDeleted = false")
    long countPhotosByOwnerId(
        @Param("ownerId") int ownerId);

    @Query("SELECT COALESCE(SUM(o.amount), 0) " +
           "FROM Order o " +
           "WHERE o.client.owner.id = :ownerId " +
           "AND o.status = 'paid'")
    java.math.BigDecimal getTotalRevenueByOwnerId(
        @Param("ownerId") int ownerId);
}