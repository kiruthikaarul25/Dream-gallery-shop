package com.shop.service;

import com.shop.model.PhotoSelection;
import com.shop.repository.PhotoSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PhotoSelectionService {

    @Autowired
    private PhotoSelectionRepository photoSelectionRepository;

    // ─── Photo Select ───────────────────────────────
    public PhotoSelection selectPhoto(
            PhotoSelection selection) {
        return photoSelectionRepository.save(selection);
    }

    // ─── Client Selections ──────────────────────────
    public List<PhotoSelection> getSelectionsByClient(
            int clientId) {
        return photoSelectionRepository
            .findByClientId(clientId);
    }

    // ─── Already Selected Check ─────────────────────
    public boolean isSelected(int clientId, int photoId) {
        return photoSelectionRepository
            .existsByClientIdAndPhotoId(clientId, photoId);
    }

    // ─── Deselect ───────────────────────────────────
    @Transactional
    public void deselectPhoto(int clientId, int photoId) {
        photoSelectionRepository
            .deleteByClientIdAndPhotoId(clientId, photoId);
    }

    // ─── Count ──────────────────────────────────────
    public long countSelections(int clientId) {
        return photoSelectionRepository
            .countByClientId(clientId);
    }

    // ─── Clear All Selections ───────────────────────
    @Transactional
    public void clearAllSelections(int clientId) {
        photoSelectionRepository
            .deleteAllByClientId(clientId);
    }
}