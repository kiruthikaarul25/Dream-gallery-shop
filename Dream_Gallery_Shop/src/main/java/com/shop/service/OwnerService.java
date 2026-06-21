package com.shop.service;

import com.shop.model.Owner;
import com.shop.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Owner register(Owner owner) {
        if (ownerRepository.existsByEmail(owner.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        owner.setPasswordHash(
            passwordEncoder.encode(owner.getPasswordHash()));
        return ownerRepository.save(owner);
    }

    public Optional<Owner> findByEmail(String email) {
        return ownerRepository.findByEmail(email);
    }

    public Optional<Owner> getById(int id) {
        return ownerRepository.findById(id);
    }

    public boolean emailExists(String email) {
        return ownerRepository.existsByEmail(email);
    }

    public Owner update(Owner owner) {
        return ownerRepository.save(owner);
    }

    public List<Owner> getAll() {
        return ownerRepository.findAll();
    }

    public Owner getLoggedInOwner(String email) {
        return ownerRepository.findByEmail(email)
            .orElseThrow(() ->
                new RuntimeException("Owner not found: " + email));
    }
}