package com.shop.config;

import com.shop.model.Owner;
import com.shop.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "niki@gmail.com";

        Owner owner = ownerRepository.findByEmail(adminEmail)
            .orElse(new Owner());

        owner.setName("Admin");
        owner.setEmail(adminEmail);
        owner.setPasswordHash(passwordEncoder.encode("niki"));
        owner.setStudioName("Dream Gallery Shop");
        owner.setPhone("9999999999");
        ownerRepository.save(owner);
        System.out.println("✅ Owner saved: " + adminEmail);
    }
}