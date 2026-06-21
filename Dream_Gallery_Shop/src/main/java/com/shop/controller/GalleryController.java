package com.shop.controller;

import com.shop.model.*;
import com.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class GalleryController {

    @Autowired private ClientService clientService;
    @Autowired private PhotoService photoService;
    @Autowired private AlbumService albumService;
    @Autowired private PhotoSelectionService selectionService;
    @Autowired private PackageService packageService;

    @GetMapping("/client/login")
    public String clientLoginPage(Model model) {
        return "client/login";
    }

    @PostMapping("/client/login")
    public String clientLoginSubmit(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        return clientService.findByToken(username)
            .map(client -> {
                if (client.getAccessPassword() != null
                        && client.getAccessPassword().equals(password)) {
                    session.setAttribute("clientId", client.getId());
                    clientService.updateStatus(client.getId(), "active");
                    return "redirect:/gallery/view";
                } else {
                    model.addAttribute("error", "Invalid username or password!");
                    return "client/login";
                }
            })
            .orElseGet(() -> {
                model.addAttribute("error", "Invalid username or password!");
                return "client/login";
            });
    }

    @GetMapping("/gallery/{token}")
    public String clientTokenLogin(
            @PathVariable("token") String token,
            HttpSession session,
            Model model) {
        return clientService.findByToken(token)
            .map(client -> {
                model.addAttribute("token", token);
                model.addAttribute("coupleName", client.getCoupleName());
                return "client/login";
            })
            .orElseGet(() -> "error/invalid-link");
    }

    @PostMapping("/gallery/login")
    public String galleryLoginSubmit(
            @RequestParam("token") String token,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        return clientService.findByToken(token)
            .map(client -> {
                if (client.getAccessToken().equals(username)
                        && client.getAccessPassword() != null
                        && client.getAccessPassword().equals(password)) {
                    session.setAttribute("clientId", client.getId());
                    clientService.updateStatus(client.getId(), "active");
                    return "redirect:/gallery/view";
                } else {
                    model.addAttribute("token", token);
                    model.addAttribute("coupleName", client.getCoupleName());
                    model.addAttribute("error", "Invalid username or password!");
                    return "client/login";
                }
            })
            .orElseGet(() -> "error/invalid-link");
    }

    @GetMapping("/gallery/view")
    public String viewGallery(HttpSession session, Model model) {
        Integer clientId = (Integer) session.getAttribute("clientId");
        if (clientId == null) return "redirect:/client/login";

        Client client = clientService.getById(clientId).orElseThrow();
        List<Album> albums = albumService.getAlbumsByClient(clientId);

        Set<Integer> selectedIds = selectionService
            .getSelectionsByClient(clientId).stream()
            .map(s -> s.getPhoto().getId())
            .collect(Collectors.toSet());

        Map<Integer, List<Photo>> selectedPhotos = new LinkedHashMap<>();
        Map<Integer, List<Photo>> unselectedPhotos = new LinkedHashMap<>();

        for (Album album : albums) {
            List<Photo> all = photoService.getPhotosByAlbum(album.getId());
            selectedPhotos.put(album.getId(),
                all.stream().filter(p -> selectedIds.contains(p.getId())).collect(Collectors.toList()));
            unselectedPhotos.put(album.getId(),
                all.stream().filter(p -> !selectedIds.contains(p.getId())).collect(Collectors.toList()));
        }

        model.addAttribute("client", client);
        model.addAttribute("albums", albums);
        model.addAttribute("selectedPhotos", selectedPhotos);
        model.addAttribute("unselectedPhotos", unselectedPhotos);
        model.addAttribute("selectionCount", selectedIds.size());
        model.addAttribute("packages",
            packageService.getPackagesByOwner(client.getOwner().getId()));

        return "client/gallery";
    }

    // ✅ AJAX — no page reload
    @PostMapping("/gallery/select/{photoId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> selectPhoto(
            @PathVariable("photoId") int photoId,
            HttpSession session) {
        Integer clientId = (Integer) session.getAttribute("clientId");
        if (clientId == null)
            return ResponseEntity.status(401).build();

        if (!selectionService.isSelected(clientId, photoId)) {
            Client client = clientService.getById(clientId).orElseThrow();
            Photo photo = photoService.getById(photoId).orElseThrow();
            PhotoSelection ps = new PhotoSelection();
            ps.setClient(client);
            ps.setPhoto(photo);
            selectionService.selectPhoto(ps);
        }

        int count = selectionService.getSelectionsByClient(clientId).size();
        Map<String, Object> res = new HashMap<>();
        res.put("status", "selected");
        res.put("count", count);
        return ResponseEntity.ok(res);
    }

    // ✅ AJAX — no page reload
    @PostMapping("/gallery/deselect/{photoId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deselectPhoto(
            @PathVariable("photoId") int photoId,
            HttpSession session) {
        Integer clientId = (Integer) session.getAttribute("clientId");
        if (clientId == null)
            return ResponseEntity.status(401).build();

        selectionService.deselectPhoto(clientId, photoId);

        int count = selectionService.getSelectionsByClient(clientId).size();
        Map<String, Object> res = new HashMap<>();
        res.put("status", "deselected");
        res.put("count", count);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/gallery/submit")
    public String submitSelections(HttpSession session) {
        Integer clientId = (Integer) session.getAttribute("clientId");
        if (clientId == null) return "redirect:/client/login";

        clientService.updateStatus(clientId, "selecting");
        // PRG Pattern: redirect after POST so Back button works cleanly
        return "redirect:/gallery/submitted";
    }

    @GetMapping("/gallery/submitted")
    public String submittedPage(HttpSession session, Model model) {
        Integer clientId = (Integer) session.getAttribute("clientId");
        if (clientId == null) return "redirect:/client/login";

        var selections = selectionService.getSelectionsByClient(clientId);
        model.addAttribute("selections", selections);
        model.addAttribute("count", selections.size());
        model.addAttribute("client",
            clientService.getById(clientId).orElseThrow());
        return "client/submitted";
    }
}