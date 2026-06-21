package com.shop.config;

import com.shop.model.Owner;
import com.shop.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Owner owner = ownerRepository
            .findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "Owner not found: " + email));

        return new User(
            owner.getEmail(),
            owner.getPasswordHash(),
            Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_OWNER"))
        );
    }
}