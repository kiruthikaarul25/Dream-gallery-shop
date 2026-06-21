package com.shop.controller;

import com.shop.service.*;
import com.shop.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PhotoService photoService;

    // ─── Dashboard ──────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        model.addAttribute("owner", owner);
        model.addAttribute("clientCount",
            clientService.countByOwner(owner.getId()));
        model.addAttribute("photoCount",
            photoService.countByOwner(owner.getId()));
        model.addAttribute("totalRevenue", 0);
        model.addAttribute("paidOrders", 0);
        model.addAttribute("recentClients",
            clientService.findByOwner(owner.getId()));
        return "dashboard";  // ← templates/dashboard.html
    }

    // ─── Profile Page ───────────────────────────────
    @GetMapping("/owner/profile")
    public String profilePage(Authentication auth, Model model) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        model.addAttribute("owner", owner);
        return "owner/profile";
    }

    // ─── Profile Update ─────────────────────────────
    @PostMapping("/owner/profile")
    public String updateProfile(
            @ModelAttribute Owner owner,
            Authentication auth) {
        Owner existing = ownerService.getLoggedInOwner(auth.getName());
        owner.setId(existing.getId());
        owner.setPasswordHash(existing.getPasswordHash());
        ownerService.update(owner);
        return "redirect:/owner/profile";
    }

    // ─── Stats API ──────────────────────────────────
    @GetMapping("/owner/stats")
    @ResponseBody
    public java.util.Map<String, Object> getStats(Authentication auth) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalClients", clientService.countByOwner(owner.getId()));
        stats.put("totalPhotos", photoService.countByOwner(owner.getId()));
        return stats;
    }
}