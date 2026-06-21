package com.shop.repository;

import com.shop.model.PhotoSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PhotoSelectionRepository
        extends JpaRepository<PhotoSelection, Integer> {

    List<PhotoSelection> findByClientId(int clientId);
    boolean existsByClientIdAndPhotoId(
        int clientId, int photoId);
    void deleteByClientIdAndPhotoId(
        int clientId, int photoId);
    long countByClientId(int clientId);

    @Modifying
    @Query("DELETE FROM PhotoSelection ps " +
           "WHERE ps.client.id = :clientId")
    void deleteAllByClientId(int clientId);
}