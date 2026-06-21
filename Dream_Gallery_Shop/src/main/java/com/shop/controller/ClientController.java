package com.shop.controller;

import com.shop.model.*;
import com.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired private ClientService clientService;
    @Autowired private OwnerService ownerService;
    @Autowired private AlbumService albumService;
    @Autowired private PhotoService photoService;
    @Autowired private PhotoSelectionService photoSelectionService;

    // ─── All Clients ────────────────────────────────
    @GetMapping
    public String listClients(Authentication auth, Model model) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        model.addAttribute("clients", clientService.getClientsByOwner(owner.getId()));
        model.addAttribute("owner", owner);
        return "owner/clients";
    }

    // ─── Add Client Page ────────────────────────────
    @GetMapping("/add")
    public String addClientPage(Model model) {
        model.addAttribute("client", new Client());
        return "owner/add-client";
    }

    // ─── Add Client Submit ──────────────────────────
    @PostMapping("/add")
    public String addClient(
            @ModelAttribute Client client,
            Authentication auth,
            Model model) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        client.setOwner(owner);
        Client saved = clientService.createClient(client);
        return "redirect:/photos/upload/" + saved.getId();
    }

    // ─── View Client ────────────────────────────────
    @GetMapping("/{id}")
    public String viewClient(@PathVariable("id") int id, Model model) {
        clientService.getById(id).ifPresent(client -> {
            model.addAttribute("client", client);

            List<com.shop.model.Album> albums = albumService.getAlbumsByClient(id);
            model.addAttribute("albums", albums);
            model.addAttribute("photos", photoService.findAllByClientId(id));

            java.util.Set<Integer> selectedIds = photoSelectionService
                .getSelectionsByClient(id).stream()
                .map(s -> s.getPhoto().getId())
                .collect(java.util.stream.Collectors.toSet());
            model.addAttribute("selectedIds", selectedIds);
            model.addAttribute("selectionCount", selectedIds.size());

            java.util.Map<Integer, java.util.List<com.shop.model.Photo>> selectedPhotos = new java.util.LinkedHashMap<>();
            java.util.Map<Integer, java.util.List<com.shop.model.Photo>> unselectedPhotos = new java.util.LinkedHashMap<>();

            for (com.shop.model.Album album : albums) {
                java.util.List<com.shop.model.Photo> all = photoService.getPhotosByAlbum(album.getId());
                selectedPhotos.put(album.getId(),
                    all.stream().filter(p -> selectedIds.contains(p.getId())).collect(java.util.stream.Collectors.toList()));
                unselectedPhotos.put(album.getId(),
                    all.stream().filter(p -> !selectedIds.contains(p.getId())).collect(java.util.stream.Collectors.toList()));
            }

            model.addAttribute("selectedPhotos", selectedPhotos);
            model.addAttribute("unselectedPhotos", unselectedPhotos);
        });
        return "owner/view-client";
    }

    // ─── Edit Client Page ───────────────────────────
    @GetMapping("/{id}/edit")
    public String editClientPage(@PathVariable("id") int id, Model model) {
        clientService.getById(id).ifPresent(c -> model.addAttribute("client", c));
        return "owner/edit-client";
    }

    // ─── Edit Client Submit ─────────────────────────
    @PostMapping("/{id}/edit")
    public String editClient(@PathVariable("id") int id, @ModelAttribute Client client) {
        Client existing = clientService.getById(id).orElseThrow();
        client.setId(id);
        client.setOwner(existing.getOwner());
        client.setCreatedAt(existing.getCreatedAt());
        if (client.getAccessToken() == null || client.getAccessToken().isBlank()) {
            client.setAccessToken(existing.getAccessToken());
        }
        if (client.getAccessPassword() == null || client.getAccessPassword().isBlank()) {
            client.setAccessPassword(existing.getAccessPassword());
        }
        clientService.update(client);
        return "redirect:/clients/" + id;
    }

    // ─── Delete Client ──────────────────────────────
    @GetMapping("/{id}/delete")
    public String deleteClient(@PathVariable("id") int id) {
        clientService.deleteById(id);
        return "redirect:/clients";
    }

    // ─── Send Link Page ─────────────────────────────
    @GetMapping("/{id}/send-link")
    public String sendLinkPage(@PathVariable("id") int id, Model model) {
        clientService.getById(id).ifPresent(client -> {
            String link = "http://localhost:8765/gallery/" + client.getAccessToken();
            model.addAttribute("link", link);
            model.addAttribute("client", client);

            String whatsappMsg = "Hi " + client.getCoupleName()
                + "! Your wedding photos are ready. "
                + "View here: " + link
                + " | Username: " + client.getAccessToken()
                + " | Password: " + client.getAccessPassword();
            String whatsappUrl = "https://wa.me/"
                + client.getPhone()
                + "?text="
                + java.net.URLEncoder.encode(whatsappMsg, java.nio.charset.StandardCharsets.UTF_8);
            model.addAttribute("whatsappUrl", whatsappUrl);
        });
        return "owner/send-link";
    }

    // ─── Update Status ──────────────────────────────
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable("id") int id, @RequestParam("status") String status) {
        clientService.updateStatus(id, status);
        return "redirect:/clients/" + id;
    }
}