package com.shop.repository;
import com.shop.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface AlbumRepository
        extends JpaRepository<Album, Integer> {

    List<Album> findByClientIdOrderByCreatedAtDesc(int clientId);

    Optional<Album> findFirstByClientIdOrderByCreatedAtDesc(int clientId);

    long countByClientId(int clientId);

    // ─── Owner methods ───────────────────────────
    List<Album> findByOwnerIdOrderByCreatedAtDesc(int ownerId);

    long countByOwnerId(int ownerId);
}