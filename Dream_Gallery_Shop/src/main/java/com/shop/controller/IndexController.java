package com.shop.controller;

import com.shop.service.PackageService;
import com.shop.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired private PackageService packageService;
    @Autowired private OwnerRepository ownerRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/work")
    public String work() {
        return "work";
    }

    @GetMapping("/packages-page")
    public String packages(Model model) {
        ownerRepository.findAll().stream().findFirst().ifPresent(owner ->
            model.addAttribute("packages", packageService.getPackagesByOwner(owner.getId()))
        );
        return "packages-public";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/select")
    public String select() {
        return "select";
    }
}