package com.shop.service;

import com.shop.model.Client;
import com.shop.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // ─── Create Client ──────────────────────────────
    public Client createClient(Client client) {
        // accessToken = username (owner sets it)
        // accessPassword = password (owner sets it)
        if (client.getAccessToken() == null || client.getAccessToken().isEmpty()) {
            client.setAccessToken(
                java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        }
        client.setStatus("pending");
        return clientRepository.save(client);
    }

    // ─── Find by Token (username) ───────────────────
    public Optional<Client> findByToken(String token) {
        return clientRepository.findByAccessToken(token);
    }

    // ─── Get by ID ──────────────────────────────────
    public Optional<Client> getById(int id) {
        return clientRepository.findById(id);
    }

    // ─── Owner-ன் Clients ───────────────────────────
    public List<Client> getClientsByOwner(int ownerId) {
        return clientRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId);
    }

    public List<Client> findByOwner(int ownerId) {
        return clientRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId);
    }

    // ─── Update ─────────────────────────────────────
    public Client update(Client client) {
        return clientRepository.save(client);
    }

    // ─── Update Status ──────────────────────────────
    public void updateStatus(int clientId, String status) {
        clientRepository.findById(clientId).ifPresent(c -> {
            c.setStatus(status);
            clientRepository.save(c);
        });
    }

    // ─── Delete ─────────────────────────────────────
    public void deleteById(int id) {
        clientRepository.deleteById(id);
    }

    // ─── Count ──────────────────────────────────────
    public long countByOwner(int ownerId) {
        return clientRepository.countByOwnerId(ownerId);
    }
}