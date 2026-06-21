package com.shop.repository;

import com.shop.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PackageRepository
        extends JpaRepository<Package, Integer> {

    List<Package> findByOwnerIdAndIsActiveTrue(int ownerId);
    List<Package> findByOwnerId(int ownerId);
}