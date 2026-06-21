package com.shop.controller;

import com.shop.model.*;
import com.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class PhotoController {

    @Autowired private PhotoService photoService;
    @Autowired private AlbumService albumService;
    @Autowired private OwnerService ownerService;
    @Autowired private ClientService clientService;
    @Autowired private OrderService orderService;
    @Autowired private PhotoSelectionService photoSelectionService;
    @Autowired private WatermarkService watermarkService;

    @Value("${app.upload.dir:uploads/photos}")
    private String uploadDir;

    // ─── Serve Watermarked Photo to Client ────────
    @GetMapping("/gallery/photo/{photoId}")
    public ResponseEntity<byte[]> serveWatermarkedPhoto(
            @PathVariable("photoId") int photoId,
            jakarta.servlet.http.HttpSession session) throws IOException {

        Integer clientId = (Integer) session.getAttribute("clientId");
        if (clientId == null)
            return ResponseEntity.status(401).build();

        Photo photo = photoService.getById(photoId).orElseThrow();
        Client client = clientService.getById(clientId).orElseThrow();

        String fullPath = System.getProperty("user.dir") + File.separator + photo.getFilePath();

        byte[] watermarked = watermarkService.addWatermark(fullPath, client.getCoupleName());

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + photo.getFilename() + "\"")
            .header("Cache-Control", "no-store, no-cache, must-revalidate")
            .header("X-Content-Type-Options", "nosniff")
            .contentType(MediaType.IMAGE_JPEG)
            .body(watermarked);
    }

    // ─── Upload Page ───────────────────────────────
    @GetMapping("/photos/upload/{clientId}")
    public String uploadPage(
            @PathVariable("clientId") int clientId,
            Authentication auth, Model model) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        Client client = clientService.getById(clientId).orElseThrow();
        model.addAttribute("client", client);
        model.addAttribute("clientId", clientId);
        model.addAttribute("albums", albumService.getAlbumsByClient(clientId));
        return "owner/upload";
    }

    // ─── Upload Submit ─────────────────────────────
    @PostMapping("/owner/upload")
    public String uploadPhotos(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("clientId") int clientId,
            @RequestParam(value = "albumId", required = false) Integer albumId,
            @RequestParam(value = "albumTitle", required = false) String albumTitle,
            Authentication auth) throws IOException {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        Client client = clientService.getById(clientId).orElseThrow();

        Album saved;
        if (albumId != null) {
            saved = albumService.getById(albumId).orElseThrow();
        } else {
            String title = (albumTitle != null && !albumTitle.isBlank()) ? albumTitle.trim() : "Untitled";
            saved = albumService.getAlbumsByClient(clientId).stream()
                .filter(a -> a.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElseGet(() -> {
                    Album album = new Album();
                    album.setTitle(title);
                    album.setOwner(owner);
                    album.setClient(client);
                    return albumService.createAlbum(album);
                });
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                photoService.uploadPhoto(file, saved, owner);
            }
        }
        return "redirect:/clients/" + clientId;
    }

    // ─── Download Selected Photos as ZIP ──────────
    @GetMapping("/owner/clients/{clientId}/download/selected")
    public ResponseEntity<byte[]> downloadSelected(
            @PathVariable("clientId") int clientId) throws IOException {

        Set<Integer> selectedIds = photoSelectionService
            .getSelectionsByClient(clientId).stream()
            .map(s -> s.getPhoto().getId())
            .collect(Collectors.toSet());

        List<Photo> allPhotos = photoService.findAllByClientId(clientId);
        List<Photo> selected = allPhotos.stream()
            .filter(p -> selectedIds.contains(p.getId()))
            .collect(Collectors.toList());

        Client client = clientService.getById(clientId).orElseThrow();
        String zipName = client.getCoupleName().replaceAll("[^a-zA-Z0-9]", "_") + "_Selected.zip";

        byte[] zipBytes = createZip(selected);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipName + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(zipBytes);
    }

    // ─── Download Unselected Photos as ZIP ────────
    @GetMapping("/owner/clients/{clientId}/download/unselected")
    public ResponseEntity<byte[]> downloadUnselected(
            @PathVariable("clientId") int clientId) throws IOException {

        Set<Integer> selectedIds = photoSelectionService
            .getSelectionsByClient(clientId).stream()
            .map(s -> s.getPhoto().getId())
            .collect(Collectors.toSet());

        List<Photo> allPhotos = photoService.findAllByClientId(clientId);
        List<Photo> unselected = allPhotos.stream()
            .filter(p -> !selectedIds.contains(p.getId()))
            .collect(Collectors.toList());

        Client client = clientService.getById(clientId).orElseThrow();
        String zipName = client.getCoupleName().replaceAll("[^a-zA-Z0-9]", "_") + "_Unselected.zip";

        byte[] zipBytes = createZip(unselected);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipName + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(zipBytes);
    }

    // ─── ZIP Helper ────────────────────────────────
    private byte[] createZip(List<Photo> photos) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Photo photo : photos) {
                Path filePath = Paths.get(System.getProperty("user.dir"), photo.getFilePath());
                if (Files.exists(filePath)) {
                    ZipEntry entry = new ZipEntry(photo.getFilename());
                    zos.putNextEntry(entry);
                    zos.write(Files.readAllBytes(filePath));
                    zos.closeEntry();
                }
            }
        }
        return baos.toByteArray();
    }

    // ─── Delete Photo ──────────────────────────────
    @PostMapping("/owner/photos/{photoId}/delete")
    public String deletePhoto(
            @PathVariable("photoId") int photoId,
            @RequestParam(value = "clientId", required = false) Integer clientId) {
        photoService.deletePhoto(photoId);
        if (clientId != null) {
            return "redirect:/clients/" + clientId;
        }
        return "redirect:/clients";
    }

    // ─── Create Folder Page ────────────────────────
    @GetMapping("/owner/folders/new")
    public String newFolderPage(Authentication auth, Model model) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        model.addAttribute("clients", clientService.findByOwner(owner.getId()));
        return "owner/create-folder";
    }

    // ─── Create Folder Submit ──────────────────────
    @PostMapping("/owner/folders/new")
    public String createFolder(
            @RequestParam("name") String name,
            @RequestParam("clientId") int clientId,
            Authentication auth) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        Client client = clientService.getById(clientId).orElseThrow();
        Album album = new Album();
        album.setTitle(name);
        album.setOwner(owner);
        album.setClient(client);
        albumService.createAlbum(album);
        return "redirect:/photos/upload/" + clientId;
    }

    // ─── Orders Page ───────────────────────────────
    @GetMapping("/owner/orders")
    public String ordersPage(Authentication auth, Model model) {
        Owner owner = ownerService.getLoggedInOwner(auth.getName());
        model.addAttribute("orders", orderService.findByOwner(owner.getId()));
        return "owner/orders";
    }
}