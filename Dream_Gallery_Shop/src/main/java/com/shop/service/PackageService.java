package com.shop.service;

import com.shop.model.Package;
import com.shop.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    // ─── Package Create ─────────────────────────────
    public Package createPackage(Package pkg) {
        pkg.setIsActive(true);
        return packageRepository.save(pkg);
    }

    // ─── Save (alias) ───────────────────────────────
    public Package save(Package pkg) {
        return packageRepository.save(pkg);
    }

    // ─── Owner Active Packages ──────────────────────
    public List<Package> getPackagesByOwner(int ownerId) {
        return packageRepository
            .findByOwnerIdAndIsActiveTrue(ownerId);
    }

    // ─── All Packages (including inactive) ──────────
    public List<Package> getAllByOwner(int ownerId) {
        return packageRepository.findByOwnerId(ownerId);
    }

    // ─── Get by ID ──────────────────────────────────
    public Optional<Package> getById(int id) {
        return packageRepository.findById(id);
    }

    // ─── Update ─────────────────────────────────────
    public Package update(Package pkg) {
        return packageRepository.save(pkg);
    }

    // ─── Soft Delete ────────────────────────────────
    public void deletePackage(int id) {
        packageRepository.findById(id).ifPresent(pkg -> {
            pkg.setIsActive(false);
            packageRepository.save(pkg);
        });
    }

    // ─── Hard Delete ────────────────────────────────
    public void hardDelete(int id) {
        packageRepository.deleteById(id);
    }

    // ─── Delete by ID (alias) ───────────────────────
    public void deleteById(int id) {
        deletePackage(id);
    }
}