package com.shop.repository;

import com.shop.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhotoRepository
        extends JpaRepository<Photo, Integer> {

    // Album photos (not deleted)
    List<Photo> findByAlbumIdAndIsDeletedFalse(
            int albumId);

    // Client-ன் all photos (not deleted)
    List<Photo> findByAlbum_Client_IdAndIsDeletedFalse(
            int clientId);

    // Owner photo count
    long countByOwnerIdAndIsDeletedFalse(int ownerId);
}