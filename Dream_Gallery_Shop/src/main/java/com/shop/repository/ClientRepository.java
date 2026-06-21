package com.shop.repository;

import com.shop.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClientRepository
        extends JpaRepository<Client, Integer> {

    List<Client> findByOwnerIdOrderByCreatedAtDesc(
        int ownerId);
    Optional<Client> findByAccessToken(String token);
    long countByOwnerId(int ownerId);
}