package com.shop.service;

import com.shop.model.Album;
import com.shop.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    // ─── Album Create ───────────────────────────────
    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    // ─── Client Albums ──────────────────────────────
    public List<Album> getAlbumsByClient(int clientId) {
        return albumRepository
            .findByClientIdOrderByCreatedAtDesc(clientId);
    }

    // ─── Latest Album ───────────────────────────────
    public Optional<Album> getLatestAlbum(int clientId) {
        return albumRepository
            .findFirstByClientIdOrderByCreatedAtDesc(clientId);
    }

    // ─── ID மூலம் Find ──────────────────────────────
    public Optional<Album> getById(int id) {
        return albumRepository.findById(id);
    }

    // ─── Update ─────────────────────────────────────
    public Album update(Album album) {
        return albumRepository.save(album);
    }

    // ─── Delete ─────────────────────────────────────
    public void deleteById(int id) {
        albumRepository.deleteById(id);
    }

    // ─── Count ──────────────────────────────────────
    public long countByClient(int clientId) {
        return albumRepository.countByClientId(clientId);
    }
 // ─── Find by Owner ──────────────────────────────
    public List<Album> findByOwner(int ownerId) {
        return albumRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId);
    }

    // ─── Count by Owner ─────────────────────────────
    public long countByOwner(int ownerId) {
        return albumRepository.countByOwnerId(ownerId);
    }
    public List<Album> getAlbumsByOwner(int ownerId) {
        return albumRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId);
    }
}